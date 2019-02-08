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
   public final static int     PROCESSOR_AutoThreshold= 0;   
   public final static String PROCESSOR_AutoTresholdKey = "Threshold";

   private Studio studio_;

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

