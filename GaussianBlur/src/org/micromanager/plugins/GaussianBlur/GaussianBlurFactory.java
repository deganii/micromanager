/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.micromanager.plugins.GaussianBlur;

/**
 *
 * @author BillBill
 */
import org.micromanager.LogManager;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorFactory;
import org.micromanager.PropertyMap;
import org.micromanager.Studio;

public class GaussianBlurFactory implements ProcessorFactory {

   private final Studio studio_;
   private final PropertyMap settings_;
   private final LogManager log_;

   public GaussianBlurFactory(Studio studio, PropertyMap settings) {
      studio_ = studio;
      settings_ = settings;
      log_ = studio_.logs();
   }

   @Override
   public Processor createProcessor() {      
      return new GaussianBlur(studio_, settings_);
   }
}
