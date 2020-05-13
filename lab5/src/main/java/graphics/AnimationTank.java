package graphics;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.vecmath.*;

public class AnimationTank implements ActionListener, KeyListener {
    private Button go;
    private TransformGroup wholeTank;
    private Transform3D transform;
    private Transform3D rotateTransformX;
    private Transform3D rotateTransformY;
    private Transform3D rotateTransformZ;
    
    private JFrame mainFrame;


    private float xloc = 1;
    private float yloc = -0.7f;
    private float zloc = 0;


    private Timer timer;

    private float scale_cur = 0.5f;
    private float speed_cur = 0.005f;
    private int count_iter = 1;
    
    public AnimationTank(TransformGroup wholeTank, Transform3D trans, JFrame frame){
        go = new Button("Go");
        this.wholeTank=wholeTank;
        this.transform=trans;
        this.mainFrame=frame;

        
        rotateTransformX= new Transform3D();
        rotateTransformY= new Transform3D();
        rotateTransformZ= new Transform3D();
        
        Main.canvas.addKeyListener(this);
        timer = new Timer(100, this);
        
        Panel p =new Panel();
        p.add(go);
        mainFrame.add("North",p);
        go.addActionListener(this);
        go.addKeyListener(this);
    }

    private void initialPlaneState(){
//        xloc=0.0f;
//        yloc=0.0f;
//        zloc=0.0f;
//        rotateTransformY.rotY(-Math.PI/2.8);
//        transform.mul(rotateTransformY);
        if(timer.isRunning()){timer.stop();}
        go.setLabel("Go");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // start timer when button is pressed
       if (e.getSource()==go){
          if (!timer.isRunning()) {
             timer.start();
             go.setLabel("Stop");
          }
          else {
              timer.stop();
              go.setLabel("Go");
          }
       }
       else {

           Move();
       }
    }

    private void Move() {

        transform.setTranslation(new Vector3f(xloc, yloc, zloc));
        transform.setScale(scale_cur);
        wholeTank.setTransform(transform);

        xloc -= 0.005 + count_iter * 0.002;
        yloc -= 0.001 + count_iter * 0.0005;
        zloc -= 0.005 + count_iter * 0.002;

        count_iter += 1;
        scale_cur += 0.002;

        if (yloc < -1.4) {
            xloc = 1.2f;
            yloc = -0.64f;
            zloc = 0.2f;
            scale_cur = 0.5f;
            count_iter = 1;
        }

    }
    
    @Override
    public void keyTyped(KeyEvent e) {
          //Invoked when a key has been typed.
    }

    @Override
    public void keyPressed(KeyEvent e) {
         //Invoked when a key has been pressed.
        if (!timer.isRunning()) {
            if (e.getKeyChar()=='w') {
                rotateTransformY.rotY(degreeToRad(1));
                transform.mul(rotateTransformY);
                wholeTank.setTransform(transform);
            }
            if (e.getKeyChar()=='s') {
                rotateTransformY.rotY(degreeToRad(-1));
                transform.mul(rotateTransformY);
                wholeTank.setTransform(transform);
            }
        }
        
        if (e.getKeyChar()=='a') {
            zloc -= 0.01;
            xloc -= 0.01;
            transform.setTranslation(new Vector3f(xloc, yloc, zloc));
            wholeTank.setTransform(transform);
        }

        if (e.getKeyChar()=='d') {
            zloc += 0.01;
            xloc += 0.01;
            System.out.println(zloc);
            transform.setTranslation(new Vector3f(xloc, yloc, zloc));
            wholeTank.setTransform(transform);
        }
    }

    private double degreeToRad(double degree) {
        return 0.0174533 * degree;
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // Invoked when a key has been released.
    }
    
}
