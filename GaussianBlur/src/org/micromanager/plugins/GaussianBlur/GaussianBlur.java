/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.micromanager.plugins.GaussianBlur;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.Options;
import ij.plugin.PlugIn;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.Binary;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.RoiManager;
import ij.process.AutoThresholder;
import ij.process.BinaryProcessor;
import ij.process.ByteProcessor;
import ij.process.FloatPolygon;
import ij.process.ImageProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;
import org.micromanager.LogManager;
import org.micromanager.Studio;
import org.micromanager.PropertyMap;
import org.micromanager.data.Coords;
import org.micromanager.data.Image;
import org.micromanager.data.Metadata;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorContext;
import org.micromanager.data.SummaryMetadata;
import org.micromanager.projector.internal.devices.SLM;

/**
 *
 * @author BillBill
 */
public class GaussianBlur extends Processor {
   private final Studio studio_;
   private final PropertyMap setting_;
   //private final LogManager log_;
   public GaussianBlur(Studio studio, PropertyMap setting){
       studio_ = studio;
       setting_ = setting;
          
    }

    @Override
    public void processImage(Image image, ProcessorContext pc) {        
        Coords coords = image.getCoords();
        Metadata meta = image.getMetadata();
        ImageProcessor ip = studio_.data().ij().createProcessor(image);      
        
        if(setting_.getBoolean(GaussianBlurPlugin.PROCESSOR_useGaussianBlurKey, GaussianBlurPlugin.PROCESSOR_useGaussianBlur))
            ip.blurGaussian(setting_.getDouble(GaussianBlurPlugin.PROCESSOR_RadiusKey, GaussianBlurPlugin.PROCESSOR_Radius));  
        
        if(setting_.getBoolean(GaussianBlurPlugin.PROCESSOR_useThresKey, GaussianBlurPlugin.PROCESSOR_useThres))        {
            ip.setAutoThreshold(AutoThresholder.Method.values()[setting_.getInteger(GaussianBlurPlugin.PROCESSOR_AutoTresholdKey, GaussianBlurPlugin.PROCESSOR_AutoThreshold)], false);
            ip.autoThreshold();
            if(setting_.getBoolean(GaussianBlurPlugin.PROCESSOR_useAnalyzeKey, GaussianBlurPlugin.PROCESSOR_useAnalyze))
            {
                BinaryProcessor bp = new BinaryProcessor(new ByteProcessor(ip, false));
                if(setting_.getBoolean(GaussianBlurPlugin.PROCESSOR_useOpeningKey, GaussianBlurPlugin.PROCESSOR_useOpening)){
                    bp.erode();
                    bp.dilate();
                }
                if(setting_.getBoolean(GaussianBlurPlugin.PROCESSOR_useClosingKey, GaussianBlurPlugin.PROCESSOR_useClosing)){
                    bp.dilate();
                    bp.erode();
                }
                RoiManager roiManager = RoiManager.getInstance();
                int measures = Measurements.AREA + Measurements.CENTER_OF_MASS;
                int options = ParticleAnalyzer.ADD_TO_MANAGER + ParticleAnalyzer.CLEAR_WORKSHEET;// ParticleAnalyzer.CLEAR_WORKSHEET + ParticleAnalyzer.SHOW_RESULTS + ParticleAnalyzer.ADD_TO_MANAGER;
                ResultsTable rt = ResultsTable.createTableFromImage(ip);
                double minSize = setting_.getLong(GaussianBlurPlugin.PROCESSOR_Analyze_MinSizeKey, GaussianBlurPlugin.PROCESSOR_Analyze_MinSize);
                double maxSize = setting_.getLong(GaussianBlurPlugin.PROCESSOR_Analyze_MaxSizeKey, GaussianBlurPlugin.PROCESSOR_Analyze_MaxSize);
                double minCir = setting_.getDouble(GaussianBlurPlugin.PROCESSOR_Analyze_MinCirKey, GaussianBlurPlugin.PROCESSOR_Analyze_MinCir);
                double maxCir = setting_.getDouble(GaussianBlurPlugin.PROCESSOR_Analyze_MaxCirKey, GaussianBlurPlugin.PROCESSOR_Analyze_MaxCir);

                ParticleAnalyzer pa = new ParticleAnalyzer(options, measures, rt, minSize, maxSize, minCir, maxCir);
                pa.analyze(new ImagePlus("analyzed", ip));        
                studio_.alerts().postAlert("Test", GaussianBlur.class, AutoThresholder.getMethods()[setting_.getInteger(GaussianBlurPlugin.PROCESSOR_AutoTresholdKey, GaussianBlurPlugin.PROCESSOR_AutoThreshold)]);
                SLM slm = new SLM(studio_, studio_.core(), 20);
                slm.turnOn();
                ij.gui.Roi roi[] = roiManager.getRoisAsArray();

                List<FloatPolygon> roiPolygon = new ArrayList<FloatPolygon>();

                for (int i=0; i<roi.length; i++){             
                    roiPolygon.add(roi[i].getFloatPolygon());        
                }
              //  List<FloatPolygon> transformedRois = ProjectorActions.transformROIs(roi, mapping_);
                slm.loadRois(roiPolygon); 
                
                ip = bp;
            }
        }
         pc.outputImage(studio_.data().ij().createImage(ip, coords, meta));
    }
}
