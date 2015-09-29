package week7.modeling.extrusion;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import week7.modeling.extrusion.MatrixMath;
import week7.modeling.extrusion.Point;
import week7.modeling.extrusion.Polygon;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * An example of Mesh extrusions for COMP3421 
 * 
 * @author malcolmr
 */
public class ExtrusionExample extends JFrame implements ItemListener {

    // how much the cross-section is scaled before extrusion
    private static final double SCALE = 0.5;
    
    // UI components
    private GLJPanel[] myPanels;
    private ButtonGroup myCrossSectionSelection;
    private ButtonGroup mySpineSelection;

    // dictionaries of available cross-sections and spines
    private Map<String, Polygon> myCrossSections;
    private Map<String, List<Point>> mySpines;

    // the current extruded mesh
    private List<Polygon> myMesh = null;

    public ExtrusionExample() {
        super("Extrusion");
        init();
    }

    public void init() {
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        setSize(1200, 400);

        initCrossSections();
        initSpines();
        initMenus();
        initPanels(caps);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void initPanels(GLCapabilities caps) {
        // create panels to draw on
        myPanels = new GLJPanel[3];
        myPanels[0] = new GLJPanel(caps);
        myPanels[1] = new GLJPanel(caps);
        myPanels[2] = new GLJPanel(caps);

        setLayout(new GridLayout(1, 3));
        add(myPanels[0]);
        add(myPanels[1]);
        add(myPanels[2]);

        // The cross section view
        CrossSectionView view0 = new CrossSectionView(this);
        myPanels[0].addGLEventListener(view0);

        // the spine view
        SpineView view1 = new SpineView(this);
        myPanels[1].addGLEventListener(view1);
        myPanels[1].addMouseMotionListener(view1);

        // the extrusion view
        ExtrusionView view2 = new ExtrusionView(this);
        myPanels[2].addGLEventListener(view2);
        myPanels[2].addMouseMotionListener(view2);

        FPSAnimator animator = new FPSAnimator(60);
        animator.add(myPanels[0]);
        animator.add(myPanels[1]);
        animator.add(myPanels[2]);
        animator.start();
    }

    /**
     * Create menus to selct cross-section and spine
     *
     */
    private void initMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu crosssectionMenu = new JMenu("Cross-section");
        menuBar.add(crosssectionMenu);

        myCrossSectionSelection = new ButtonGroup();
        for (String s : myCrossSections.keySet()) {
            JMenuItem item = new JRadioButtonMenuItem(s);
            crosssectionMenu.add(item);
            myCrossSectionSelection.add(item);
            item.addItemListener(this);
        }
        myCrossSectionSelection.setSelected(myCrossSectionSelection.getElements().nextElement().getModel(), true);

        JMenu spineMenu = new JMenu("Spine");
        menuBar.add(spineMenu);

        mySpineSelection = new ButtonGroup();
        for (String s : mySpines.keySet()) {
            JMenuItem item = new JRadioButtonMenuItem(s);
            spineMenu.add(item);
            mySpineSelection.add(item);
            item.addItemListener(this);
        }
        mySpineSelection.setSelected(mySpineSelection.getElements().nextElement().getModel(), true);

        setJMenuBar(menuBar);
    }

    /**
     * Create available cross-sections
     *
     */
    private void initCrossSections() {
        myCrossSections = new LinkedHashMap<String, Polygon>();

        // An isosceles triangle
        
        Polygon triangle = new Polygon();
        triangle.addPoint(SCALE, 0, 0);
        triangle.addPoint(-SCALE, 0.5 * SCALE, 0);
        triangle.addPoint(-SCALE, -0.5 * SCALE, 0);
        myCrossSections.put("Triangle", triangle);

        // A square
        
        Polygon square = new Polygon();
        square.addPoint(SCALE, SCALE, 0);
        square.addPoint(-SCALE, SCALE, 0);
        square.addPoint(-SCALE, -SCALE, 0);
        square.addPoint(SCALE, -SCALE, 0);
        myCrossSections.put("Square", square);

        // A circle
        
        Polygon circle = new Polygon();
        for (int i = 0; i < 16; i++) {
            double a = Math.PI * 2 * i / 16;
            circle.addPoint(SCALE* Math.cos(a), SCALE* Math.sin(a), 0);
        }
        myCrossSections.put("Circle", circle);
        
    }

    /**
     * Create available spines
     *
     */
    private void initSpines() {
        mySpines = new LinkedHashMap<String, List<Point>>();

        // a simple line segment
        
        List<Point> line = new ArrayList<Point>();
        line.add(new Point(-1, 0, 0));
        line.add(new Point(1, 0, 0));
        mySpines.put("Line", line);
        
        // three sides of a square
        
        List<Point> square = new ArrayList<Point>();
        square.add(new Point(1, 1, 0));
        square.add(new Point(-1, 1, 0));
        square.add(new Point(-1, -1, 0));
        square.add(new Point(1, -1, 0));
        mySpines.put("Square", square);

        // a semi-circle
        
        List<Point> semicircle = new ArrayList<Point>();
        for (int i = 0; i <= 16; i++) {
            double a = Math.PI * i / 16;
            semicircle.add(new Point(Math.cos(a), Math.sin(a), 0));
        }
        mySpines.put("Semicircle", semicircle);

        // a sine wave
        
        List<Point> sine = new ArrayList<Point>();
        for (int i = 0; i <= 64; i++) {
            double a = Math.PI * i / 32;
            sine.add(new Point(i / 32.0 - 1, Math.cos(a), 0));
        }
        mySpines.put("Sine wave", sine);

        // a helix
        
        List<Point> helix = new ArrayList<Point>();
        for (int i = 0; i <= 128; i++) {
            double a = Math.PI * i / 16;
            helix.add(new Point(Math.cos(a), Math.sin(a), i / 16.0 - 4));
        }
        mySpines.put("Helix", helix);

    }

    /**
     * Get the name of the current menu selections
     * 
     * @param group
     * @return
     */
    private String getSelection(ButtonGroup group) {
        Enumeration<AbstractButton> e = group.getElements();

        while (e.hasMoreElements()) {
            AbstractButton button = e.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }

    /**
     * Get the currently selected cross section
     * 
     * @return
     */
    public Polygon getCrossSection() {

        String s = getSelection(myCrossSectionSelection);

        if (s == null) {
            return null;
        }

        return myCrossSections.get(s);
    }

    /**
     * Get the currently selected spine
     * 
     * @return
     */
    public List<Point> getSpine() {
        String s = getSelection(mySpineSelection);

        if (s == null) {
            return null;
        }

        return mySpines.get(s);
    }
    
    /**
     * Get the extruded mesh
     * 
     * @return
     */
    public List<Polygon> getMesh() {
        // compute the mesh if necessary
        if (myMesh == null) {
            computeMesh();
        }
        return myMesh;
    }
    
    /**
     * The extrusion code.
     * 
     * This method extrudes the cross section along the spine
     *
     */
    private void computeMesh() {
               
        Polygon cs = getCrossSection();
        if (cs == null) {
            return;
        }
        
        List<Point> crossSection = cs.getPoints();
        List<Point> spine = getSpine();
        if (spine == null) {
            return;
        }

        //
        // Step 1: create a vertex list by moving the cross section along the spine
        //
        
        List<Point> vertices = new ArrayList<Point>();

        Point pPrev;
        Point pCurr = spine.get(0);
        Point pNext = spine.get(1);
        
        // first point is a special case
        addPoints(crossSection, vertices, pCurr, pCurr, pNext);
        
        // mid points
        for (int i = 1; i < spine.size() - 1; i++) {
            pPrev = pCurr;
            pCurr = pNext;
            pNext = spine.get(i+1);
            addPoints(crossSection, vertices, pPrev, pCurr, pNext);            
        }
        
        // last point is a special case
        pPrev = pCurr;
        pCurr = pNext;
        addPoints(crossSection, vertices, pPrev, pCurr, pCurr);

        // 
        // Step 2: connect points in successive cross-sections to form quads
        // 
        
        myMesh = new ArrayList<Polygon>();

        int n = crossSection.size();
        
        // for each point along the spine
        for (int i = 0; i < spine.size() - 1; i++) {

            // for each point in the cross section
            for (int j = 0; j < n; j++) {
                // create a quad joining this point and the next one
                // to the equivalent points in the next cross-section
                
                // note: make sure they are in anti-clockwise order
                // so they are facing outwards
                
                Polygon quad = new Polygon();                
                quad.addPoint(vertices.get(i * n + j));
                quad.addPoint(vertices.get(i * n + (j+1) % n));
                quad.addPoint(vertices.get((i+1) * n + (j+1) % n));
                quad.addPoint(vertices.get((i+1) * n + j));
                myMesh.add(quad);
            }
            
        }
        
    }

    /**
     * Transform the points in the cross-section using the Frenet frame
     * and add them to the vertex list.
     * 
     * @param crossSection The cross section
     * @param vertices The vertex list
     * @param pPrev The previous point on the spine
     * @param pCurr The current point on the spine
     * @param pNext The next point on the spine
     */
    private void addPoints(List<Point> crossSection, List<Point> vertices,
            Point pPrev, Point pCurr, Point pNext) {

        // compute the Frenet frame as an affine matrix
        double[][] m = new double[4][4];
        
        // phi = pCurr        
        m[0][3] = pCurr.x;
        m[1][3] = pCurr.y;
        m[2][3] = pCurr.z;
        m[3][3] = 1;
        
        // k = pNext - pPrev (approximates the tangent)
       
        m[0][2] = pNext.x - pPrev.x;
        m[1][2] = pNext.y - pPrev.y;
        m[2][2] = pNext.z - pPrev.z;
        m[3][2] = 0;
      
        
        // normalise k
        double d = Math.sqrt(m[0][2] * m[0][2] + m[1][2] * m[1][2] + m[2][2] * m[2][2]);  
        m[0][2] /= d;
        m[1][2] /= d;
        m[2][2] /= d;
        
        // i = simple perpendicular to k
        m[0][0] = -m[1][2];
        m[1][0] =  m[0][2];
        m[2][0] =  0;
        m[3][0] =  0;
        
        // j = k x i
        m[0][1] = m[1][2] * m[2][0] - m[2][2] * m[1][0];
        m[1][1] = m[2][2] * m[0][0] - m[0][2] * m[2][0];
        m[2][1] = m[0][2] * m[1][0] - m[1][2] * m[0][0];
        m[3][1] =  0;
        
        // transform the points
       
        for (Point cp : crossSection) {
        
            Point q = cp.transform(m);
           
            vertices.add(q);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // selection has changed, the mesh needs to be recomputed
        // so discard the old one
        myMesh = null;
    }

    public static void main(String[] args) {
        ExtrusionExample extrusion = new ExtrusionExample();
    }

}
