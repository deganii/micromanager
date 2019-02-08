/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.micromanager.plugins.GaussianBlur;
import ij.process.AutoThresholder;
import ij.process.ImageProcessor;
import java.util.List;
import java.util.Map;
import org.micromanager.LogManager;
import org.micromanager.Studio;
import org.micromanager.PropertyMap;
import org.micromanager.data.Coords;
import org.micromanager.data.Image;
import org.micromanager.data.Metadata;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorContext;
import org.micromanager.data.SummaryMetadata;

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
        ip.blurGaussian(setting_.getDouble(GaussianBlurPlugin.PROCESSOR_RadiusKey, GaussianBlurPlugin.PROCESSOR_Radius));  
        ip.setAutoThreshold(AutoThresholder.Method.values()[setting_.getInteger(GaussianBlurPlugin.PROCESSOR_AutoTresholdKey, GaussianBlurPlugin.PROCESSOR_AutoThreshold)], false);
        studio_.alerts().postAlert("Test", GaussianBlur.class, AutoThresholder.getMethods()[setting_.getInteger(GaussianBlurPlugin.PROCESSOR_AutoTresholdKey, GaussianBlurPlugin.PROCESSOR_AutoThreshold)]);
        ip.autoThreshold();        
        pc.outputImage(studio_.data().ij().createImage(ip, coords, meta));
    }
}
