package graphics;


import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import com.sun.j3d.utils.geometry.Cylinder;
import java.awt.Container;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.vecmath.Color4f;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.sun.j3d.utils.geometry.*;

public class Main implements ActionListener {


    private TransformGroup treeTransformGroup;
    private Timer timer;
    private float angle = 0;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        timer = new Timer(50, this);
        timer.start();
        BranchGroup scene = createSceneGraph();
        SimpleUniverse u = new SimpleUniverse();
        u.getViewingPlatform().setNominalViewingTransform();
        u.addBranchGraph(scene);
    }

    public BranchGroup createSceneGraph() {

        BranchGroup objRoot = new BranchGroup();

        treeTransformGroup = new TransformGroup();
        treeTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        print();
        objRoot.addChild(treeTransformGroup);

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),100.0);
        Color3f light1Color = new Color3f(1.0f, 0.5f, 0.4f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color,	light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);

        Color3f ambientColor = new Color3f(1.0f, 1.0f, 1.0f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);
        return objRoot;
    }

    private double degreeToRad(double degree) {
        return 0.0174533 * degree;
    }

    private void print() {

        createCylinder(0.71f, 0.01f, -0.3f, 0, .2f, -34, 0, 0, "src/main/resources/tree.jpg", new Color3f((float)101/255, (float)67/255, (float)33/255));
        createCylinder(0.71f, 0.01f, -0.3f, 0, -.2f, 34, 0, 0, "src/main/resources/tree.jpg", new Color3f((float)101/255, (float)67/255, (float)33/255));
        createCylinder(0.4f, 0.008f, -0.3f, 0, 0, 90, 0, 0, "src/main/resources/tree.jpg", new Color3f((float)101/255, (float)67/255, (float)33/255));

        createCylinder(0.71f, 0.01f, 0.3f, 0, .2f, -34, 0, 0, "src/main/resources/tree.jpg", new Color3f((float)101/255, (float)67/255, (float)33/255));
        createCylinder(0.71f, 0.01f, 0.3f, 0, -.2f, 34, 0, 0, "src/main/resources/tree.jpg", new Color3f((float)101/255, (float)67/255, (float)33/255));
        createCylinder(0.4f, 0.008f, 0.3f, 0, 0, 90, 0, 0, "src/main/resources/tree.jpg", new Color3f((float)101/255, (float)67/255, (float)33/255));

        createCylinder(0.64f, 0.015f, 0, 0.3f, 0, 0, 0, 90, "src/main/resources/tree.jpg", new Color3f((float)101/255, (float)67/255, (float)33/255));
        createBox(0.1f, 0.01f, 0.1f, 0, -0.2f, 0, 0, 0, 0, "src/main/resources/board.jpg", new Color3f(0f, 47/255f, 31/255f));

        createCylinder(0.36f, 0.001f, -0.098f, 0.125f, 0, 0, 0, 0, "src/main/resources/chain.jpeg", new Color3f((float)100/255, (float)100/255, (float)100/255));
        createCylinder(0.175f, 0.001f, -0.098f, -0.125f, 0.05f, -34, 0, 0, "src/main/resources/chain.jpeg", new Color3f((float)100/255, (float)100/255, (float)100/255));
        createCylinder(0.175f, 0.001f, -0.098f, -0.125f, -0.05f, 34, 0, 0, "src/main/resources/chain.jpeg", new Color3f((float)100/255, (float)100/255, (float)100/255));


        createCylinder(0.36f, 0.001f, 0.098f, 0.125f, 0, 0, 0, 0, "src/main/resources/chain.jpeg", new Color3f((float)100/255, (float)100/255, (float)100/255));
        createCylinder(0.175f, 0.001f, 0.098f, -0.125f, 0.05f, -34, 0, 0, "src/main/resources/chain.jpeg", new Color3f((float)100/255, (float)100/255, (float)100/255));
        createCylinder(0.175f, 0.001f, 0.098f, -0.125f, -0.05f, 34, 0, 0, "src/main/resources/chain.jpeg", new Color3f((float)100/255, (float)100/255, (float)100/255));


    }

    private void createCylinder(float height, float radius, float x, float y, float z, float rotX, float rotY, float rotZ, String picture, Color3f emissive) {
        TransformGroup tg = new TransformGroup();
        Transform3D transform = new Transform3D();
        Transform3D rx = new Transform3D();
        Transform3D ry = new Transform3D();
        Transform3D rz = new Transform3D();
        Cylinder cylinder = XMass.getCylinder(height, radius, picture, emissive);
        Vector3f vector = new Vector3f(x, y, z);
        transform.setTranslation(vector);
        rx.rotX(degreeToRad(rotX));
        ry.rotY(degreeToRad(rotY));
        rz.rotZ(degreeToRad(rotZ));
        transform.mul(rx);
        transform.mul(ry);
        transform.mul(rz);
        tg.setTransform(transform);
        tg.addChild(cylinder);
        treeTransformGroup.addChild(tg);
    }

    private void createBox(float xdim, float ydim, float zdim, float x, float y, float z, float rotX, float rotY, float rotZ, String picture, Color3f emissive) {
        TransformGroup tg = new TransformGroup();
        Transform3D transform = new Transform3D();
        Transform3D rx = new Transform3D();
        Transform3D ry = new Transform3D();
        Transform3D rz = new Transform3D();
        Vector3f vector = new Vector3f(x, y, z);
        Box box = XMass.getBox(xdim, ydim, zdim, picture, emissive);
        transform.setTranslation(vector);
        rx.rotX(degreeToRad(rotX));
        ry.rotY(degreeToRad(rotY));
        rz.rotZ(degreeToRad(rotZ));
        transform.mul(rx);
        transform.mul(ry);
        transform.mul(rz);
        tg.setTransform(transform);
        tg.addChild(box);
        treeTransformGroup.addChild(tg);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Transform3D transform = new Transform3D();
        transform.rotY(degreeToRad(angle));
        if (treeTransformGroup != null) treeTransformGroup.setTransform(transform);
        angle += 1;
    }
}


class XMass {

    private static Appearance getXMassAppearence( String picture, Color3f emissive) {
        Appearance ap = new Appearance();
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);

        ap.setMaterial(new Material(emissive, black, black, black, 1.0f));
        if (picture != "") {
            TextureLoader loader = new TextureLoader(picture, "LUMINANCE", new Container());
            Texture texture = loader.getTexture();
            texture.setBoundaryModeS(Texture.WRAP);
            texture.setBoundaryModeT(Texture.WRAP);
            texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 1.0f, 0.0f));
            TextureAttributes texAttr = new TextureAttributes();
            texAttr.setTextureMode(TextureAttributes.MODULATE);
            ap.setTexture(texture);
            ap.setTextureAttributes(texAttr);
        }
        return ap;
    }

    public static Cylinder getCylinder(float height, float radius, String picture, Color3f emissiveColor) {
        int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
        return new Cylinder(radius, height, primflags, getXMassAppearence(picture, emissiveColor));
    }
    public static Box getBox(float xdim, float ydim, float zdim, String picture, Color3f emissiveColor) {
        int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
        return new Box(xdim, ydim, zdim, primflags, getXMassAppearence(picture, emissiveColor));
    }
}