

/**
 * ExampleFrame.java
 *
 * This module shows an example of creating a GUI (Graphical User Interface).
 * There are many ways to do this in Java; this particular example uses the
 * MigLayout layout manager, which has extensive documentation online.
 *
 *
 * Nico Stuurman, copyright UCSF, 2012, 2015
 *
 * LICENSE: This file is distributed under the BSD license. License text is
 * included with the source distribution.
 *
 * This file is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.
 *
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
 */

package org.micromanager.plugins.PatternedLight;
import com.google.common.eventbus.Subscribe;
import ij.process.AutoThresholder;
import ij.process.ImageProcessor;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import static java.awt.image.ImageObserver.PROPERTIES;
import static java.awt.image.ImageObserver.PROPERTIES;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import mmcorej.DeviceType;

import net.miginfocom.swing.MigLayout;
import org.micromanager.MultiStagePosition;
import org.micromanager.PositionList;
import org.micromanager.StagePosition;

import org.micromanager.data.Image;
import org.micromanager.events.ExposureChangedEvent;
import org.micromanager.Studio;
import org.micromanager.data.Coords;
import org.micromanager.data.DataProvider;
import org.micromanager.data.Datastore;
import org.micromanager.data.Metadata;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorPlugin;
import org.micromanager.display.DataViewer;
import org.micromanager.internal.utils.MMFrame;

public class PatternedLightFrame extends MMFrame {

   private Studio studio_;
   private JTextField userText_;
   private final JLabel imageInfoLabel_;
   private final JLabel exposureTimeLabel_;
   private int n=0;

   public PatternedLightFrame(Studio studio) {
      super("PatternedLightFrame Plugin GUI");
      studio_ = studio;
      super.setLayout(new MigLayout("fill, insets 2, gap 2, flowx"));

      JLabel title = new JLabel("I'm an example plugin!");
      title.setFont(new Font("Arial", Font.BOLD, 16));
      super.add(title, "span, alignx center, wrap");

      // Create a text field for the user to customize their alerts.
      super.add(new JLabel("Alert text: "));
      userText_ = new JTextField(30);
      userText_.setText("Something happened!");
      super.add(userText_);      
      
      
      JButton alertButton = new JButton("Alert me!");
      // Clicking on this button will invoke the ActionListener, which in turn
      // will show a text alert to the user.
      alertButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
                    
            // Use the contents of userText_ as the text.          
            studio_.alerts().postAlert("Test",
               PatternedLightFrame.class, userText_.getText());
         }
      });
      super.add(alertButton, "wrap");
     
      //Call Stitching image function
      JButton StitchingButton = new JButton("Stitching");
      // Clicking on this button will invoke the ActionListener, which in turn
      // will show a text alert to the user.
      StitchingButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {                    
            // Ask for the Top Left and Lower Right Coordinate of the Stitching image
            String XYstageName = studio_.core().getXYStageDevice();
            String ZstageName = studio_.core().getFocusDevice();
            PositionList c = new PositionList();
            double[] PosX = null;
            double[] PosY = null;
            int counter = 0;
            try {                 
                 studio_.core().getXYPosition(XYstageName, PosX, PosY);
                 c.addPosition(counter, new MultiStagePosition("X", studio_.core().getPosition("X"),
                                                                studio_.core().getPosition("Y"),          
                                                                "Z", studio_.core().getPosition("Z")));
             } catch (Exception ex) {
                 Logger.getLogger(PatternedLightFrame.class.getName()).log(Level.SEVERE, null, ex);
             }
                     
             
             //Move the Stage and Focus
            try {
                for(int i=0; i < c.getNumberOfPositions(); i++){               
                    studio_.core().setXYPosition(XYstageName, c.getPosition(i).getX(), c.getPosition(i).getY());
                    studio_.core().setPosition(ZstageName, c.getPosition(i).getZ());                
                }
                while(studio_.core().deviceTypeBusy(DeviceType.StageDevice))
                    studio_.core().waitForDeviceType(DeviceType.StageDevice);
            } catch (Exception ex) {
                    Logger.getLogger(PatternedLightFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      });
      super.add(StitchingButton, "wrap");
      
      // Snap an image, show the image in the Snap/Live view, and show some
      // stats on the image in our frame.
      imageInfoLabel_ = new JLabel();
      super.add(imageInfoLabel_, "growx, split, span");
      JButton snapButton = new JButton("Start Live");
      snapButton.addActionListener(new ActionListener() {
         private ScheduledExecutorService timer;
         @Override
         public void actionPerformed(ActionEvent e) {
            studio_.alerts().postAlert("PatternedLight",
               PatternedLightFrame.class, "Capture Video");
            try {                 
                studio_.core().setProperty("OpenCVgrabber", "PixelType", "8bit");
            } catch (Exception ex) {
                Logger.getLogger(PatternedLightFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            Datastore data = studio_.data().createRAMDatastore();
            n=0;
            Runnable  a;
             a = new Runnable(){
            @Override
            public void run() {
                try {
                   
                      n++;
                      studio_.live().setLiveMode(true);
                 
                      data.putImage(studio_.live().snap(false).get(0));
                      studio_.alerts().postAlert("Snaped", PatternedLightFrame.class, n +" image "+ data.getNumImages());
                   } catch (IOException ex) {
                      Logger.getLogger(PatternedLightFrame.class.getName()).log(Level.SEVERE, null, ex);
                  }       
                try {
                    studio_.displays().show(data.getImage(data.getMaxIndices()));
                } catch (IOException ex) {
                    Logger.getLogger(PatternedLightFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                  studio_.displays().createDisplay(data);
                  studio_.app().refreshGUI();
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
                  this.timer = Executors.newSingleThreadScheduledExecutor();
             try {                 
                 this.timer.scheduleAtFixedRate(a, 0, 50, TimeUnit.MILLISECONDS);
                  } catch (Exception ex) {
                 Logger.getLogger(PatternedLightFrame.class.getName()).log(Level.SEVERE, null, ex);
             }
             
//Image firstImage = images.get(0);
//showImageInfo(firstImage);
            
              };
      });
      
      super.add(snapButton, "wrap");

      exposureTimeLabel_ = new JLabel("");
      super.add(exposureTimeLabel_, "split, span, growx");

      // Run an acquisition using the current MDA parameters.
      JButton acquireButton = new JButton("Run Acquisition");
      acquireButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // All GUI event handlers are invoked on the EDT (Event Dispatch
            // Thread). Acquisitions are not allowed to be started from the
            // EDT. Therefore we must make a new thread to run this.
            Thread acqThread = new Thread(new Runnable() {
               @Override
               public void run() {
                  studio_.acquisitions().runAcquisition();
               }
            });
            acqThread.start();
         }
      });
      super.add(acquireButton, "wrap");

      super.pack();

      // Registering this class for events means that its event handlers
      // (that is, methods with the @Subscribe annotation) will be invoked when
      // an event occurs. You need to call the right registerForEvents() method
      // to get events; this one is for the application-wide event bus, but
      // there's also Datastore.registerForEvents() for events specific to one
      // Datastore, and DisplayWindow.registerForEvents() for events specific
      // to one image display window.
      studio_.events().registerForEvents(this);
   }

   /**
    * To be invoked, this method must be public and take a single parameter
    * which is the type of the event we care about.
    * @param event
    */
   @Subscribe
   public void onExposureChanged(ExposureChangedEvent event) {
      exposureTimeLabel_.setText(String.format("Camera %s exposure time set to %.2fms",
               event.getCameraName(), event.getNewExposureTime()));
   }

   /**
    * Display some information on the data in the provided image.
    */
   private void showImageInfo(Image image) {
      // See DisplayManager for information on these parameters.
      //HistogramData data = studio_.displays().calculateHistogram(
      //   image, 0, 16, 16, 0, true);
      imageInfoLabel_.setText(String.format(
            "Image size: %dx%d", // min: %d, max: %d, mean: %d, std: %.2f",
            image.getWidth(), image.getHeight() ) ); //, data.getMinVal(),
            //data.getMaxVal(), data.getMean(), data.getStdDev()));
   }
}
