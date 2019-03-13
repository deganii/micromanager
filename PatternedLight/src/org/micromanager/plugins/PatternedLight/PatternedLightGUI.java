/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.micromanager.plugins.PatternedLight;
import com.jogamp.opengl.math.geom.AABBox;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.macro.MacroRunner;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.RoiManager;
import ij.process.AutoThresholder;
import ij.process.BinaryProcessor;
import ij.process.ByteProcessor;
import ij.process.FloatPolygon;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;
import mmcorej.CMMCore;
import mmcorej.DeviceType;
import mmcorej.StrVector;
import mmcorej.TaggedImage;
import org.joda.time.JodaTimePermission;
import org.joda.time.format.DateTimeFormat;
import org.micromanager.MultiStagePosition;
import org.micromanager.PositionList;
import org.micromanager.PropertyMap;
import org.micromanager.Studio;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.alerts.Alert;
import org.micromanager.alerts.UpdatableAlert;
import org.micromanager.alerts.internal.DefaultAlert;
import org.micromanager.alerts.internal.DefaultAlertManager;
import org.micromanager.data.Coords;
import org.micromanager.data.DataProvider;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;
import org.micromanager.data.Metadata;
import org.micromanager.data.Pipeline;
import org.micromanager.data.RewritableDatastore;
import org.micromanager.data.internal.DefaultCoords;
import org.micromanager.data.internal.DefaultDatastore;
import org.micromanager.data.internal.DefaultImage;
import org.micromanager.data.internal.DefaultMetadata;
import org.micromanager.data.internal.DefaultRewritableDatastore;
import org.micromanager.display.DataViewer;
import org.micromanager.display.DisplayWindow;
import org.micromanager.internal.positionlist.utils.TileCreator;
import org.micromanager.internal.positionlist.utils.ZGenerator;
import org.micromanager.internal.propertymap.DefaultPropertyMap;
import org.micromanager.internal.utils.MMFrame;
import org.micromanager.internal.utils.ReportingUtils;
import org.micromanager.projector.ProjectionDevice;
import org.micromanager.projector.ProjectorActions;
import org.micromanager.projector.internal.Calibrator;
import org.micromanager.projector.internal.Mapping;
import org.micromanager.projector.internal.ProjectorControlForm;
import org.micromanager.projector.internal.Terms;
import org.micromanager.projector.internal.devices.SLM;
import org.micromanager.propertymap.MutablePropertyMapView;
import ucar.nc2.ft.point.collection.UpdateableCollection;

/**
 *
 * @author BillBill
 */
public class PatternedLightGUI extends MMFrame {

    /**
     * Creates new form PatternedLightGUI
     */
    private Studio studio_;
    private String XYstageName;
    private String ZstageName;  
    private AxisList axisList_;
    private TileCreator tileCreator;
    private double totalDx, totalDy, totalDz;
    private int counter = 0;
    private int n = 0;
    private DefaultListModel<String> _listModel;
    private CMMCore mmc;
    private JFileChooser fileChooser = new JFileChooser();
    private ProjectionDevice dev_;
    private final MutablePropertyMapView settings_;
    private Calibrator cal;        
    private Map<Polygon, AffineTransform> mapping_;
    private final String workingDir = System.getProperty("user.dir");
    private Calibrator calibrator_;
    
    public PatternedLightGUI(Studio studio){
        super("PatternedLightFrame Plugin GUI");        
        studio_ = studio;        
        mmc = studio_.core();
        initComponents();
        _listModel = new DefaultListModel<String>();    
        jList_PositionList.setModel(_listModel);
        XYstageName = mmc.getXYStageDevice();        
        ZstageName = mmc.getFocusDevice();        
        studio_.events().registerForEvents(this);   
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setCurrentDirectory(new File(workingDir));
        settings_ = studio_.profile().getSettings(this.getClass());
        dev_ = ProjectorActions.getProjectionDevice(studio_);        
        cal = new Calibrator(studio_, dev_, settings_);   
        mapping_ = Mapping.loadMapping(mmc, dev_, settings_);
        for(int i=0; i<AutoThresholder.getMethods().length; i++)
            jComboBox_ThresholdMethod.addItem(AutoThresholder.getMethods()[i]);
        jComboBox_ThresholdMethod.setSelectedIndex(6);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinner_IllumTime1 = new javax.swing.JSpinner();
        jLabel_ms1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_PositionList = new javax.swing.JList<>();
        jButton_add = new javax.swing.JButton();
        jButton_Remove = new javax.swing.JButton();
        jButton_Clear = new javax.swing.JButton();
        jLabel_Status = new javax.swing.JLabel();
        jButton_Stitch = new javax.swing.JButton();
        jButton_Calibration = new javax.swing.JButton();
        jButton_MoveStage = new javax.swing.JButton();
        jSpinner_MoveDelay = new javax.swing.JSpinner();
        jLabel_ms2 = new javax.swing.JLabel();
        jLabel_MoveDelay = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButton_TestMarco = new javax.swing.JButton();
        jSpinner_MinSize = new javax.swing.JSpinner();
        jLabel_MinSize = new javax.swing.JLabel();
        jLabel_MaxSize = new javax.swing.JLabel();
        jSpinner_MaxSize = new javax.swing.JSpinner();
        jSpinner_MaxCirc = new javax.swing.JSpinner();
        jLabel_MinCirc = new javax.swing.JLabel();
        jLabel_MaxCirc = new javax.swing.JLabel();
        jSpinner_MinCirc = new javax.swing.JSpinner();
        jLabel_AnalyzeParticle = new javax.swing.JLabel();
        jLabel_GaussianBlur = new javax.swing.JLabel();
        jLabel_ThresholdMethod = new javax.swing.JLabel();
        jLabel_AnalyzeParticle1 = new javax.swing.JLabel();
        jSpinner_BlurRadius = new javax.swing.JSpinner();
        jComboBox_ThresholdMethod = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jButton_ShowImage = new javax.swing.JButton();
        jSpinner_IllumTime = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel_ms = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        jSpinner_IllumTime1.setModel(new javax.swing.SpinnerNumberModel(5000, 0, null, 1));
        jSpinner_IllumTime1.setToolTipText("");

        jLabel_ms1.setText("ms");

        jLabel3.setText("illumin Time");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setViewportView(jList_PositionList);

        jButton_add.setText("Add");
        jButton_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_addActionPerformed(evt);
            }
        });

        jButton_Remove.setText("Remove");
        jButton_Remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RemoveActionPerformed(evt);
            }
        });

        jButton_Clear.setText("Clear");
        jButton_Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ClearActionPerformed(evt);
            }
        });

        jLabel_Status.setText("Add Top Left corner position");

        jButton_Stitch.setText("Start Experiment");
        jButton_Stitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_StitchActionPerformed(evt);
            }
        });

        jButton_Calibration.setText("Calibrate");
        jButton_Calibration.setToolTipText("");
        jButton_Calibration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CalibrationActionPerformed(evt);
            }
        });

        jButton_MoveStage.setText("Move Stage");
        jButton_MoveStage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_MoveStageActionPerformed(evt);
            }
        });

        jSpinner_MoveDelay.setModel(new javax.swing.SpinnerNumberModel(5000, 0, null, 1));
        jSpinner_MoveDelay.setToolTipText("");

        jLabel_ms2.setText("ms");

        jLabel_MoveDelay.setText("Move Delay");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel_Status, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(210, 210, 210))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton_add)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Remove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Clear)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton_Stitch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_Calibration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_MoveStage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jSpinner_MoveDelay, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_ms2))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel_MoveDelay)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_add)
                            .addComponent(jButton_Remove)
                            .addComponent(jButton_Clear)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton_Stitch, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_Calibration, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jButton_MoveStage, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel_MoveDelay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jSpinner_MoveDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_ms2))))
                .addGap(8, 8, 8)
                .addComponent(jLabel_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Stitch", jPanel3);

        jButton_TestMarco.setText("Test Marco File");
        jButton_TestMarco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_TestMarcoActionPerformed(evt);
            }
        });

        jSpinner_MinSize.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel_MinSize.setText("MinSize");

        jLabel_MaxSize.setText("Max Size");

        jSpinner_MaxSize.setModel(new javax.swing.SpinnerNumberModel(999999999, 0, null, 1));
        jSpinner_MaxSize.setToolTipText("");

        jSpinner_MaxCirc.setModel(new javax.swing.SpinnerNumberModel(1.0d, 0.0d, 1.0d, 0.001d));
        jSpinner_MaxCirc.setToolTipText("");

        jLabel_MinCirc.setText("Min Circ");

        jLabel_MaxCirc.setText("Max Circ");

        jSpinner_MinCirc.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 1.0d, 0.001d));

        jLabel_AnalyzeParticle.setText("Analyze Particle");

        jLabel_GaussianBlur.setText("Blur Radius");

        jLabel_ThresholdMethod.setText("Threshold");

        jLabel_AnalyzeParticle1.setText("Image Processing");

        jSpinner_BlurRadius.setModel(new javax.swing.SpinnerNumberModel(2.0d, 0.0d, null, 0.1d));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton_TestMarco))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel_MinCirc)
                            .addComponent(jLabel_MinSize))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinner_MinSize, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinner_MinCirc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(77, 77, 77)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel_MaxSize)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner_MaxSize, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel_MaxCirc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner_MaxCirc, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel_ThresholdMethod))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel_GaussianBlur)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSpinner_BlurRadius, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jComboBox_ThresholdMethod, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 42, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel_AnalyzeParticle1)
                    .addComponent(jLabel_AnalyzeParticle))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_AnalyzeParticle1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner_BlurRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_GaussianBlur))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_ThresholdMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_ThresholdMethod))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_AnalyzeParticle)
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_MaxSize)
                            .addComponent(jSpinner_MaxSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_MaxCirc)
                            .addComponent(jSpinner_MaxCirc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinner_MinSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_MinSize))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinner_MinCirc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_MinCirc))))
                .addGap(18, 18, 18)
                .addComponent(jButton_TestMarco)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Image Processing", jPanel5);

        jButton_ShowImage.setText("Show Image");
        jButton_ShowImage.setToolTipText("");
        jButton_ShowImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ShowImageActionPerformed(evt);
            }
        });

        jSpinner_IllumTime.setModel(new javax.swing.SpinnerNumberModel(5000, 0, null, 1));
        jSpinner_IllumTime.setToolTipText("");

        jLabel2.setText("illumin Time");

        jLabel_ms.setText("ms");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_ShowImage)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_IllumTime, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_ms)))
                .addContainerGap(276, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jSpinner_IllumTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel_ms))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 159, Short.MAX_VALUE)
                .addComponent(jButton_ShowImage)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Projector", jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_StitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_StitchActionPerformed
        acqThread stitching = new acqThread();
        stitching.start();
    }//GEN-LAST:event_jButton_StitchActionPerformed

    private void jButton_ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ClearActionPerformed
        studio_.positions().getPositionList().clearAllPositions();
        _listModel.removeAllElements();
        jList_PositionList.removeAll();
        n = 0;
        totalDx = 0;
        totalDy = 0;
        totalDz = 0;
        jList_PositionList.updateUI();
        jLabel_Status.setText("Please add the top left position!");
    }//GEN-LAST:event_jButton_ClearActionPerformed

    private void jButton_RemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RemoveActionPerformed
        if(studio_.positions().getPositionList().getNumberOfPositions()> 0){
            studio_.positions().getPositionList().removePosition(jList_PositionList.getSelectedIndex());
            _listModel.remove(jList_PositionList.getSelectedIndex());
            jList_PositionList.updateUI();
        }
        if(n > 0)
            n--;
        else
            jButton_RemoveActionPerformed(evt);
    }//GEN-LAST:event_jButton_RemoveActionPerformed

    private void jButton_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_addActionPerformed
        // Ask for the Top Left and Lower Right Coordinate of the Stitching image
        try {
            Point2D.Double xyPos = mmc.getXYStagePosition();
            MultiStagePosition currentPos = new MultiStagePosition(XYstageName, xyPos.x, xyPos.y, ZstageName, mmc.getPosition(ZstageName));
            studio_.positions().getPositionList().addPosition(currentPos);
            n++;
            String text = currentPos.getX() +", "+ currentPos.getY() + ", " + currentPos.getZ();
            _listModel.addElement(text);
        } catch (Exception ex) {
            Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(n == 1){
            jLabel_Status.setText("Please add the top right position!");
        }
        if(n == 2){
            jLabel_Status.setText("Please add the bottom right position!");
        }
        else if(n == 3){            
            PositionList PositionList = studio_.positions().getPositionList();
            totalDx = PositionList.getPosition(n-1).getX() - PositionList.getPosition(0).getX();
            totalDy = PositionList.getPosition(n-1).getY() - PositionList.getPosition(0).getY();
            totalDz = PositionList.getPosition(n-1).getZ() - PositionList.getPosition(0).getZ();
            //Dividing the selected area into tiles
            double pixel2um = mmc.getPixelSizeUm();
            double dX = mmc.getImageWidth() * pixel2um, dY = mmc.getImageHeight() * pixel2um;

            int numOfCol = (int)Math.ceil(totalDx / dX), numOfRow = (int)Math.ceil(totalDy / dY);
            double XdZ = (PositionList.getPosition(n-2).getZ()
                - PositionList.getPosition(0).getZ() ) / numOfCol;
            double YdZ = (PositionList.getPosition(n-1).getZ()
                - PositionList.getPosition(n-2).getZ() ) / numOfRow;

            PositionList tempPos = new PositionList();
            double FirstPosX = PositionList.getPosition(0).getX(),
            FirstPosY = PositionList.getPosition(0).getY(),
            FirstPosZ = PositionList.getPosition(0).getZ();
            for(int j=0; j <= numOfRow; j++){
                for(int i=0; i <= numOfCol;i++){
                    tempPos.addPosition(new MultiStagePosition(XYstageName, FirstPosX + dX * i, FirstPosY + dY * j, ZstageName, FirstPosZ + XdZ * i + YdZ *j ));
                    tempPos.setLabel(tempPos.getNumberOfPositions()-1, "("+i+" "+j+")");
                }
            }
            PositionList.setPositions(tempPos.getPositions());
            _listModel.removeAllElements();
            for(int i=0; i<PositionList.getNumberOfPositions(); i++){
                String text = PositionList.getPosition(i).getX() +", "
                + PositionList.getPosition(i).getY() + ", "
                + PositionList.getPosition(i).getZ();
                _listModel.addElement(text);
            }            
            jLabel_Status.setText("Number of Tiles: "+ (int)(numOfCol + 1) + " X " + (int)(numOfRow + 1) + " Images, " + dX * numOfCol + " um X " + dY * numOfRow + " um");
        }
    }//GEN-LAST:event_jButton_addActionPerformed
    public boolean isCalibrating() {
      if (calibrator_ == null) {
         return false;
      }
      return calibrator_.isCalibrating();
   }
     public void runCalibration() {
      settings_.putString("Delay", "200");   
      if (calibrator_ != null && calibrator_.isCalibrating()) {
         return;
      }
       calibrator_ = new Calibrator(studio_, dev_, settings_);
      Future<Boolean> runCalibration = calibrator_.runCalibration();
      new Thread() {
         @Override
         public void run() {
            Boolean success;
            try {
                success = runCalibration.get();
            } catch (InterruptedException | ExecutionException ex) {
               success = false;
            }
            if (success) {
               mapping_ = Mapping.loadMapping(mmc, dev_, settings_);
            }
            JOptionPane.showMessageDialog(IJ.getImage().getWindow(), "Calibration "
                       + (success ? "finished." : "canceled."));
            jButton_Calibration.setText("Calibrate");
            calibrator_ = null;
         }
      }.start();

   }
    private void jButton_CalibrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CalibrationActionPerformed
        try {
               boolean running = isCalibrating();                
               if (running) {
                  if (calibrator_ != null) {
                    calibrator_.requestStop();
                 }
                  jButton_Calibration.setText("Calibrate");
               } else {
                  runCalibration();
                  jButton_Calibration.setText("Stop calibration");
               }
            } catch (Exception e) {
               ReportingUtils.showError(e);
            }
    }//GEN-LAST:event_jButton_CalibrationActionPerformed

    private void jButton_ShowImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ShowImageActionPerformed
        SLM slm_ = new SLM(studio_, mmc, 20);
        slm_.turnOn();
        RoiManager roiManager = RoiManager.getInstance();
        ij.gui.Roi roi[] = roiManager.getRoisAsArray();

        List<FloatPolygon> roiPolygon = new ArrayList<FloatPolygon>();

        for (int i=0; i<roi.length; i++){
            roiPolygon.add(roi[i].getFloatPolygon());
        }
        //  List<FloatPolygon> transformedRois = ProjectorActions.transformROIs(roi, mapping_);
        slm_.loadRois(roiPolygon);
    }//GEN-LAST:event_jButton_ShowImageActionPerformed

    private void jButton_TestMarcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_TestMarcoActionPerformed
        //        JFileChooser open = new JFileChooser();
        //        open.setCurrentDirectory(new File(workingDir + "\\macro"));
        //        open.setDialogTitle("Open Macro");
        //        open.showOpenDialog(null);
        //        IJ.runMacroFile(open.getSelectedFile().getPath());
        IJ.runMacroFile(workingDir +"\\macro\\macro.ijm");
    }//GEN-LAST:event_jButton_TestMarcoActionPerformed

    private void jButton_MoveStageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_MoveStageActionPerformed
        movThread a = new movThread();
        a.run();
    }//GEN-LAST:event_jButton_MoveStageActionPerformed
    private void runMoveStage(){        
      //  Future<Boolean> runMoveStage = runMoveStage();
        new Thread() {
            @Override
            public void run(){
                for(int i=0; i<studio_.positions().getPositionList().getNumberOfPositions(); i++){
                    UpdatableAlert alert = studio_.alerts().postUpdatableAlert("Patterned Light Progress", "0/"+studio_.positions().getPositionList().getNumberOfPositions()+" Locations Finished");
                    MultiStagePosition tempPos = studio_.positions().getPositionList().getPosition(i);
                    try {
                        mmc.setXYPosition(XYstageName,tempPos.getX(), tempPos.getY());
                        mmc.setPosition(ZstageName, tempPos.getZ());
                        while(mmc.deviceBusy(XYstageName) || mmc.deviceBusy(ZstageName)){
                            mmc.waitForDevice(XYstageName);
                            mmc.waitForDevice(ZstageName);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }                
                    alert.setText((int)(i+1) +"/"+studio_.positions().getPositionList().getNumberOfPositions()+" Locations Finished!");

                    try {
                        Thread.sleep((int)jSpinner_MoveDelay.getValue());
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }               
                }
            
            }
        };
    }

    class movThread extends Thread{
        @Override
        public void run(){
            for(int i=0; i<studio_.positions().getPositionList().getNumberOfPositions(); i++){
                UpdatableAlert alert = studio_.alerts().postUpdatableAlert("Patterned Light Progress", "0/"+studio_.positions().getPositionList().getNumberOfPositions()+" Locations Finished");
                MultiStagePosition tempPos = studio_.positions().getPositionList().getPosition(i);
                try {
                    mmc.setXYPosition(XYstageName,tempPos.getX(), tempPos.getY());
                    mmc.setPosition(ZstageName, tempPos.getZ());
                    while(mmc.deviceBusy(XYstageName) || mmc.deviceBusy(ZstageName)){
                        mmc.waitForDevice(XYstageName);
                        mmc.waitForDevice(ZstageName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
                }                
                alert.setText((int)(i+1) +"/"+studio_.positions().getPositionList().getNumberOfPositions()+" Locations Finished!");
                                
                try {
                    Thread.sleep((int)jSpinner_MoveDelay.getValue());
                } catch (InterruptedException ex) {
                    Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
                }               
            }
        }
    }
    class acqThread extends Thread {
        public ImageProcessor ImageProcessing(ImageProcessor ip){
            ip.blurGaussian((double)jSpinner_BlurRadius.getValue());            
            ip.setAutoThreshold(AutoThresholder.Method.values()[jComboBox_ThresholdMethod.getSelectedIndex()], false);
            ip.autoThreshold();                        
            ip.erode();
            ip.dilate();
            return ip;
        }
        
        @Override
	public void run() {
            //Move the Stage and Focus, Obtain tiled images
            String path = studio_.data().getUniqueSaveDirectory(workingDir+"\\Experiment");
            SequenceSettings currSettings = studio_.acquisitions().getAcquisitionSettings();
            currSettings.usePositionList = true;
            currSettings.numFrames = 1;
            studio_.acquisitions().setAcquisitionSettings(currSettings);
            Datastore data = studio_.acquisitions().runAcquisition();            
            data.setName("Tiled Images");
            
//            fileChooser.setDialogTitle("Save the images as TIF at...");
//            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            fileChooser.showSaveDialog(null);
//            org.joda.time.DateTime time = new org.joda.time.DateTime(System.currentTimeMillis());
            try {                    
//                    System.out.println(fileChooser.getSelectedFile().getCanonicalPath()+
//                            "\\"+time.toString(DateTimeFormat.forPattern("yyyyMMdd HHmmss.SSS")));
//                    data.save(Datastore.SaveMode.MULTIPAGE_TIFF, studio_.data().getUniqueSaveDirectory(fileChooser.getSelectedFile().getCanonicalPath()));                    
                    data.save(Datastore.SaveMode.MULTIPAGE_TIFF, path + "\\TileImage");
                } catch (IOException ex) {
                    Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            try {
                mmc.setShutterOpen(!mmc.getShutterOpen());
                while(mmc.deviceBusy(mmc.getShutterDevice()))
                    Thread.sleep(500);
            } catch (Exception ex) {
                Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
             ImageProcessor ip = null;
             List<Image>  img = null;
             Datastore processedImg = studio_.data().createRAMDatastore();
             processedImg.setName("Processed Images ");
            try {                    
                    img = data.getImagesMatching(new DefaultCoords.Builder().c(0).z(0).t(0).build());
                    // Process every Tiled Image with the ImageProcessing function, 
                    // then put them to processedImg.
                    for(int i=0; i<img.size(); i++){
                        ip = studio_.data().ij().createProcessor(img.get(i));                        
                        Coords tempCoords = img.get(i).getCoords();
                        Metadata tempMetadata = img.get(i).getMetadata();
                        //Image Segmentation
                        ip = ImageProcessing(ip);
//                        jButton_TestMarcoActionPerformed(new ActionEvent(null, i, null));
                        processedImg.putImage(studio_.data().ij().createImage(ip, tempCoords, tempMetadata));                        
                      }
                    data.save(Datastore.SaveMode.MULTIPAGE_TIFF, path + "\\Processed");
                     // studio_.displays().createDisplay(studio_.album().getDatastore());
                } catch (IOException ex) {
                Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            DisplayWindow dw = studio_.displays().createDisplay(processedImg);
            
            //Move Stage and Illuminate Pattern Light
 
            dw.setCustomTitle("Illumination pattern "+ 0 +"/"+studio_.positions().getPositionList().getNumberOfPositions());
            UpdatableAlert alert = studio_.alerts().postUpdatableAlert("Patterned Light Progress", "0/"+studio_.positions().getPositionList().getNumberOfPositions()+" Locations Finished");
            for(int i=0; i<studio_.positions().getPositionList().getNumberOfPositions(); i++){
                dw.setDisplayPosition(new DefaultCoords.Builder().p(i).c(0).z(0).t(0).build());
                try {
                   ip = studio_.data().ij().createProcessor(processedImg.getImage(new DefaultCoords.Builder().p(i).c(0).z(0).t(0).build()));
                } catch (IOException ex) {
                    Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                BinaryProcessor bp = new BinaryProcessor(ip.convertToByteProcessor());
                MultiStagePosition tempPos = studio_.positions().getPositionList().getPosition(i);
                try {
                    mmc.setXYPosition(XYstageName,tempPos.getX(), tempPos.getY());
                    mmc.setPosition(ZstageName, tempPos.getZ());
                    while(mmc.deviceBusy(XYstageName) || mmc.deviceBusy(ZstageName)){
                        mmc.waitForDevice(XYstageName);
                        mmc.waitForDevice(ZstageName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                int measures = 0;// Measurements.AREA + Measurements.CENTER_OF_MASS;
                int options = ParticleAnalyzer.ADD_TO_MANAGER + ParticleAnalyzer.CLEAR_WORKSHEET;// ParticleAnalyzer.CLEAR_WORKSHEET + ParticleAnalyzer.SHOW_RESULTS + ParticleAnalyzer.ADD_TO_MANAGER;
                ResultsTable rt = ResultsTable.createTableFromImage(bp);
                double minSize = (int)jSpinner_MinSize.getValue();
                double maxSize = (int)jSpinner_MaxSize.getValue();
                double minCir = (double)jSpinner_MinCirc.getValue();
                double maxCir = (double)jSpinner_MaxCirc.getValue();
                ParticleAnalyzer pa = new ParticleAnalyzer(options, measures, rt, minSize, maxSize, minCir, maxCir);
                pa.analyze(new ImagePlus("analyzed " +i, bp));            
                RoiManager roiManager = RoiManager.getInstance();
                alert.setText((int)(i+1) +"/"+studio_.positions().getPositionList().getNumberOfPositions()+" Locations Finished!");
                
                dev_.turnOn();
                ij.gui.Roi roi[] = roiManager.getRoisAsArray();
                List<FloatPolygon> roiPolygon = new ArrayList<FloatPolygon>();

                for (int j=0; j<roi.length; j++){             
                    roiPolygon.add(roi[j].getFloatPolygon());        
                }                                              
                processedImg.setName("Illumination pattern "+ (int)(i+1) +"/"+studio_.positions().getPositionList().getNumberOfPositions());              
                dev_.turnOn();
                List<FloatPolygon> transformedRois;
                if(mapping_ != null)
                    transformedRois = ProjectorActions.transformROIs(roi, mapping_);
                dev_.loadRois(roiPolygon); 
                dev_.runPolygons();
                
                try {
                    Thread.sleep((int)jSpinner_IllumTime.getValue());
                } catch (InterruptedException ex) {
                    Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
                }               
                dev_.turnOff();
            }
            
              
//            try {
//
//                for(int i=0; i < studio_.positions().getPositionList().getNumberOfPositions(); i++){
//                    MultiStagePosition tempPos = studio_.positions().getPositionList().getPosition(i);
//
//                    mmc.waitForDevice(XYstageName);
//                    System.out.println(i+" "+tempPos.getX() + " " + tempPos.getY());
//                    mmc.setXYPosition(XYstageName, tempPos.getX(), tempPos.getY());
//
//                    mmc.waitForDevice(ZstageName);
//                    mmc.setPosition(tempPos.getZ());      
//                    System.out.println(tempPos.getZ());
//
//                    mmc.waitForDevice(mmc.getCameraDevice());
//                    mmc.waitForImageSynchro();
//                    img.addAll(studio_.live().snap(true));
//
//                    jLabel_Status.setText("Progress: "+ (int)(i+1)+ "/"+ studio_.positions().getPositionList().getNumberOfPositions() + " Images taken.");
//                    ip.add(studio_.data().ij().createProcessor(img.get(i)));
//                }
//                
//                studio_.album().addImages(img);
//                //studio_.album().getDatastore().save(Datastore.SaveMode.MULTIPAGE_TIFF, "C:\\haha");
//            } catch (Exception ex) {
//                    Logger.getLogger(PatternedLightGUI.class.getName()).log(Level.SEVERE, null, ex);
//            }        
            
            
            
        }
    }
    
       //From PositionListDlg  
    private class AxisData {
      private boolean use_;
      private final String axisName_;
      
      public AxisData(boolean use, String axisName) {
         use_ = use;
         axisName_ = axisName;
      }
      public boolean getUse() {return use_;}
      public String getAxisName() {return axisName_;}  
      public void setUse(boolean use) {use_ = use;}
   }
    
    //From PositionListDlg
    public class AxisList {
      private ArrayList<AxisData> axisList_;
      
      public AxisList() {
         this.axisList_ = new ArrayList<AxisData>();
         // Initialize the axisList.
         try {
            // add 1D stages
            StrVector stages = mmc.getLoadedDevicesOfType(DeviceType.StageDevice);
            for (int i=0; i<stages.size(); i++) {
               axisList_.add(new AxisData(true, stages.get(i)));
            }
         } catch (Exception e) {
            
         }
      }
      private AxisData get(int i) {
         if (i >=0 && i < axisList_.size()) {
            return axisList_.get(i);
         }
         return null;
      }
      private int getNumberOfPositions() {
         return axisList_.size();
      }
      public boolean use(String axisName) {
         for (int i=0; i< axisList_.size(); i++) {
            if (axisName.equals(get(i).getAxisName())) {
               return get(i).getUse();
            }
         }
         // not in the list??  It might be time to refresh the list.  
         return true;
      }         
   }
    
    //From PositionListDlg
    private class AxisTableModel extends AbstractTableModel {
      private boolean isEditable_ = true;
      public final String[] COLUMN_NAMES = new String[] {
            "Use",
            "Axis"
      };
      
      @Override
      public int getRowCount() {
         return axisList_.getNumberOfPositions();
      }
      @Override
      public int getColumnCount() {
         return COLUMN_NAMES.length;
      }
      @Override
      public String getColumnName(int columnIndex) {
         return COLUMN_NAMES[columnIndex];
      }
      @Override
      public Object getValueAt(int rowIndex, int columnIndex) {
         AxisData aD = axisList_.get(rowIndex);
         if (aD != null) {
            if (columnIndex == 0) {
               return aD.getUse();
            } else if (columnIndex == 1) {
               return aD.getAxisName();
            }
         }
         return null;
      }
      @Override
      public Class<?> getColumnClass(int c) {
         return getValueAt(0, c).getClass();
      }
      public void setEditable(boolean state) {
         isEditable_ = state;
         if (state) {
            for (int i=0; i < getRowCount(); i++) {
               
            }
         }
      }
      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex) {
         if (columnIndex == 0) {
            return isEditable_;
         }
         return false;
      }
      @Override
      public void setValueAt(Object value, int rowIndex, int columnIndex) {
         if (columnIndex == 0) {
            axisList_.get(rowIndex).setUse( (Boolean) value);
           // prefs_.putBoolean(axisList_.get(rowIndex).getAxisName(), (Boolean) value); 
         }
         fireTableCellUpdated(rowIndex, columnIndex);
//         axisTable_.clearSelection();
      }
   }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Calibration;
    private javax.swing.JButton jButton_Clear;
    private javax.swing.JButton jButton_MoveStage;
    private javax.swing.JButton jButton_Remove;
    private javax.swing.JButton jButton_ShowImage;
    private javax.swing.JButton jButton_Stitch;
    private javax.swing.JButton jButton_TestMarco;
    private javax.swing.JButton jButton_add;
    private javax.swing.JComboBox<String> jComboBox_ThresholdMethod;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel_AnalyzeParticle;
    private javax.swing.JLabel jLabel_AnalyzeParticle1;
    private javax.swing.JLabel jLabel_GaussianBlur;
    private javax.swing.JLabel jLabel_MaxCirc;
    private javax.swing.JLabel jLabel_MaxSize;
    private javax.swing.JLabel jLabel_MinCirc;
    private javax.swing.JLabel jLabel_MinSize;
    private javax.swing.JLabel jLabel_MoveDelay;
    private javax.swing.JLabel jLabel_Status;
    private javax.swing.JLabel jLabel_ThresholdMethod;
    private javax.swing.JLabel jLabel_ms;
    private javax.swing.JLabel jLabel_ms1;
    private javax.swing.JLabel jLabel_ms2;
    private javax.swing.JList<String> jList_PositionList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner_BlurRadius;
    private javax.swing.JSpinner jSpinner_IllumTime;
    private javax.swing.JSpinner jSpinner_IllumTime1;
    private javax.swing.JSpinner jSpinner_MaxCirc;
    private javax.swing.JSpinner jSpinner_MaxSize;
    private javax.swing.JSpinner jSpinner_MinCirc;
    private javax.swing.JSpinner jSpinner_MinSize;
    private javax.swing.JSpinner jSpinner_MoveDelay;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
