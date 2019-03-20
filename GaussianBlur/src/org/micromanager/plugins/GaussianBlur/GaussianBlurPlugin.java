package org.micromanager.plugins.GaussianBlur;

import org.micromanager.data.ProcessorConfigurator;
import org.micromanager.data.ProcessorFactory;
import org.micromanager.data.ProcessorPlugin;
import org.micromanager.PropertyMap;
import org.micromanager.Studio;

import org.scijava.plugin.SciJavaPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = ProcessorPlugin.class)
public class GaussianBlurPlugin implements ProcessorPlugin, SciJavaPlugin {

   public final static String MENU_NAME = "Gaussian Blur";
   public final static String TOOL_TIP_DESCRIPTION = "Gaussian Blur an image and output it";
   public final static String VERSION_NUMBER = "1.0";
   public final static String COPYRIGHT = "Bill Chow";

   public final static double PROCESSOR_Radius = 1.0d;
   public final static String PROCESSOR_RadiusKey = "Radius";
   public final static int    PROCESSOR_AutoThreshold= 0;   
   public final static String PROCESSOR_AutoTresholdKey = "Threshold";
   public final static long    PROCESSOR_Analyze_MinSize= 0L;   
   public final static String PROCESSOR_Analyze_MinSizeKey = "Analyze MinSize";
   public final static long    PROCESSOR_Analyze_MaxSize = 9999999L;   
   public final static String PROCESSOR_Analyze_MaxSizeKey = "Analyze MaxSize";
   public final static double    PROCESSOR_Analyze_MinCir= 0.d;   
   public final static String PROCESSOR_Analyze_MinCirKey = "Analyze MinCir";
   public final static double    PROCESSOR_Analyze_MaxCir= 1.d;   
   public final static String PROCESSOR_Analyze_MaxCirKey = "Analyze MaxCir";
   public final static String PROCESSOR_useGaussianBlurKey = "Use Gaussian";
   public final static boolean PROCESSOR_useGaussianBlur = true;
   public final static String PROCESSOR_useThresKey = "Use Threshold";
   public final static boolean PROCESSOR_useThres = true;
   public final static String PROCESSOR_useAnalyzeKey = "Use Analyze";
   public final static boolean PROCESSOR_useAnalyze = true;
   public final static String PROCESSOR_useOpeningKey = "Use Opening";
   public final static boolean PROCESSOR_useOpening = true;
   public final static String PROCESSOR_useClosingKey = "Use Closing";
   public final static boolean PROCESSOR_useClosing = false;

   private Studio studio_;

    public GaussianBlurPlugin() {
        
    }

   @Override
   public void setContext(Studio studio) {
      studio_ = studio;
   }

   @Override
   public ProcessorConfigurator createConfigurator(PropertyMap settings) {
      return new GaussianBlurConfigurator(settings, studio_);
   }

   @Override
   public ProcessorFactory createFactory(PropertyMap settings) {
      return new GaussianBlurFactory(studio_, settings);
   }

   @Override
   public String getName() {
      return MENU_NAME;
   }

   @Override
   public String getHelpText() {
      return TOOL_TIP_DESCRIPTION;
   }

   @Override
   public String getVersion() {
      return VERSION_NUMBER;
   }

   @Override
   public String getCopyright() {
      return COPYRIGHT;
   }
}

