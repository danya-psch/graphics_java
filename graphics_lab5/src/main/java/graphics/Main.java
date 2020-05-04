package graphics;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.lw3d.Lw3dLoader;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Map;

public class Main extends JFrame {
    static SimpleUniverse universe;
    static Scene scene;
    static Map<String, Shape3D> nameMap;
    static BranchGroup root;
//    static BranchGroup factoryGroup;
    static Canvas3D canvas;

    static TransformGroup wholeTank = new TransformGroup();
    static TransformGroup wholeFactory = new TransformGroup();
    static Transform3D transform3D = new Transform3D();

    private final String nameOfTanksMeshGroup = "body_untitled.004";
    private final String nameOfFactoryMeshGroup = "cube_6_untitled.005";


    private float x_eye_loc = -3.985f;
    private float y_eye_loc = -0.07f;
    private float z_eye_loc = 1.635f;

    public Main () throws IOException{
        configureWindow();
        configureCanvas();
        configureUniverse();
        addModelToUniverse();
//        setTigerElementsList();
        setTankElementsList();
        setFactoryElementsList();
        addAppearance();
        addImageBackground();
        addLightToUniverse();
        addOtherLight();
//        ChangeViewAngle();
        changeViewAngle(0);
        root.compile();
        universe.addBranchGraph(root);
    }

    private void configureWindow()  {
        setTitle("Tank");
        setSize(760,640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void configureCanvas(){
        canvas=new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);
        getContentPane().add(canvas,BorderLayout.CENTER);
    }

    private void changeViewAngle(float angle) {
        ViewingPlatform vp = universe.getViewingPlatform();
        Transform3D transform = new Transform3D();
        transform.lookAt(
                new Point3d(x_eye_loc * Math.cos(angle), y_eye_loc, z_eye_loc + x_eye_loc * Math.sin(angle)),
                new Point3d(-0.7, 0, 0),
                new Vector3d(0, 1, 0)
        );
        transform.invert();
        vp.getViewPlatformTransform().setTransform(transform);
    }

    private void configureUniverse() {
        root= new BranchGroup();
        universe= new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
    }

    private void addModelToUniverse() throws IOException{
        scene = getSceneFromFile("src/main/resources/scene.obj");
//         scene=getSceneFromLwoFile("d://3dModels//Aspen.lwo");
        root=scene.getSceneGroup();
//        factoryGroup = getSceneFromFile("src/main/resources/factory/factory.obj").getSceneGroup();

    }

    private void addLightToUniverse(){
        DirectionalLight dirLight = new DirectionalLight(
                new Color3f(Color.WHITE),
                new Vector3f(4.0f, -7.0f, -12.0f)
        );
        dirLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000));
        root.addChild(dirLight);
    }

    private void printModelElementsList(Map<String,Shape3D> nameMap){
        for (String name : nameMap.keySet()) {
            System.out.printf("Name: %s\n", name);}
    }


    private void setTankInitialTransform() {
        transform3D.setIdentity();
        transform3D.setScale(0.5);
        transform3D.setTranslation(new Vector3d(1.2, -0.64, 0.2));
        wholeTank.setTransform(transform3D);
    }

    private void setFactoryInitialTransform() {
        transform3D.setIdentity();

        transform3D.rotY(Math.PI);
        transform3D.setScale(1);
        transform3D.setTranslation(new Vector3d(0, -0.4, 1));
        wholeFactory.setTransform(transform3D);
    }

    private void setInitialTransform(TransformGroup group, double rotX, double rotY, double rotZ, double scale) {
        transform3D.setIdentity();
        transform3D.rotY(Math.PI);
        transform3D.setScale(1);
        group.setTransform(transform3D);
    }

    private void setTankElementsList() {
        nameMap = scene.getNamedObjects();

        printModelElementsList(nameMap);
        setTankInitialTransform();

        root.removeChild(nameMap.get(nameOfTanksMeshGroup));
        wholeTank.addChild(nameMap.get(nameOfTanksMeshGroup));
        wholeTank.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(wholeTank);
    }

    private void setFactoryElementsList() {
        nameMap = scene.getNamedObjects();
        setFactoryInitialTransform();

        root.removeChild(nameMap.get(nameOfFactoryMeshGroup));
        wholeFactory.addChild(nameMap.get(nameOfFactoryMeshGroup));
        wholeFactory.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(wholeFactory);
    }



    Texture getTexture(String path) {
        TextureLoader textureLoader = new TextureLoader(path, canvas);
        Texture texture = textureLoader.getTexture();
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor( new Color4f( 1.0f, 1.0f, 1.0f, 1.0f ) );
        return texture;
    }

    Material getMaterial() {
        Material material = new Material();
        material.setAmbientColor(new Color3f(new Color(243, 242, 221)));
        material.setDiffuseColor(new Color3f(new Color(255, 233, 207)));
        material.setSpecularColor(new Color3f(new Color(255, 203, 195)));
        material.setLightingEnable(true);
        return material;
    }

    private void addAppearance(){

        Appearance factoryAppearance = new Appearance();
        factoryAppearance.setTexture(getTexture("src/main/resources/factory/factory.png"));
        TextureAttributes factoryTexAttr = new TextureAttributes();
        factoryTexAttr.setTextureMode(TextureAttributes.MODULATE);
        factoryAppearance.setTextureAttributes(factoryTexAttr);
        factoryAppearance.setMaterial(getMaterial());
        Shape3D factory = nameMap.get(nameOfFactoryMeshGroup); //body_untitled.004
        factory.setAppearance(factoryAppearance);


        Appearance tankAppearance = new Appearance();
        tankAppearance.setTexture(getTexture("src/main/resources/tank/tank.png"));
        TextureAttributes tankTexAttr = new TextureAttributes();
        tankTexAttr.setTextureMode(TextureAttributes.MODULATE);
        tankAppearance.setTextureAttributes(tankTexAttr);
        tankAppearance.setMaterial(getMaterial());
        Shape3D tank = nameMap.get(nameOfTanksMeshGroup); //body_untitled.004
        tank.setAppearance(tankAppearance);
    }

    private void addColorBackground(){
        Background background = new Background(new Color3f(Color.CYAN));
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),100.0);
        background.setApplicationBounds(bounds);
        root.addChild(background);
    }

    private void addImageBackground(){
        TextureLoader t = new TextureLoader("src/main/resources/space.jpg", canvas);
        Background background = new Background(t.getImage());
        background.setImageScaleMode(Background.SCALE_FIT_ALL);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),100.0);
        background.setApplicationBounds(bounds);
        root.addChild(background);
    }

    private void ChangeViewAngle(){
        ViewingPlatform vp = universe.getViewingPlatform();
        TransformGroup vpGroup = vp.getMultiTransformGroup().getTransformGroup(0);
        Transform3D vpTranslation = new Transform3D();
        Vector3f translationVector = new Vector3f(0.0F, -1.2F, 6F);
        vpTranslation.setTranslation(translationVector);
        vpGroup.setTransform(vpTranslation);
    }

    private void addOtherLight(){
        Color3f directionalLightColor = new Color3f(Color.BLACK);
        Color3f ambientLightColor = new Color3f(Color.WHITE);
        Vector3f lightDirection = new Vector3f(-1F, -1F, -1F);

        AmbientLight ambientLight = new AmbientLight(ambientLightColor);
        DirectionalLight directionalLight = new DirectionalLight(directionalLightColor, lightDirection);

        Bounds influenceRegion = new BoundingSphere();

        ambientLight.setInfluencingBounds(influenceRegion);
        directionalLight.setInfluencingBounds(influenceRegion);
        root.addChild(ambientLight);
        root.addChild(directionalLight);
    }

    public static Scene getSceneFromFile(String location) throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        file.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
        return file.load(new FileReader(location));
    }

    //Not always works
    public static Scene getSceneFromLwoFile(String location) throws IOException {
        Lw3dLoader loader = new Lw3dLoader();
        return loader.load(new FileReader(location));
    }

    public static void main(String[]args){
        try {
            Main window = new Main();
            Transform3D tr = new Transform3D();
            wholeTank.getTransform(tr);
            AnimationTank tankMovement = new AnimationTank(wholeTank, tr, window);
            window.addKeyListener(tankMovement);
            window.setVisible(true);
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}