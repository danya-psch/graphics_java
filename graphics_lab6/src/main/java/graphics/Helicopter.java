package graphics;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.swing.JFrame;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;

import java.awt.*;

public class Helicopter extends JFrame
{
    private final String helicopterPath = "src/main/resources/helicopter.obj";
    private final String backgroundPath = "src/main/resources/bg.jpg";
    public Canvas3D myCanvas3D;

    public Helicopter()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        SimpleUniverse simpUniv = new SimpleUniverse(myCanvas3D);
        simpUniv.getViewingPlatform().setNominalViewingTransform();
        createSceneGraph(simpUniv);
        addLight(simpUniv);


        OrbitBehavior ob = new OrbitBehavior(myCanvas3D);
        ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE));
        simpUniv.getViewingPlatform().setViewPlatformBehavior(ob);

        setTitle("Cockroach");
        setSize(1600,900);
        getContentPane().add("Center", myCanvas3D);
        setVisible(true);
    }

    public void createSceneGraph(SimpleUniverse su)
    {
        BranchGroup theScene = new BranchGroup();
        Background background = new Background(new TextureLoader(backgroundPath, myCanvas3D).getImage());
        background.setImageScaleMode(Background.SCALE_FIT_MAX);
        background.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), Double.MAX_VALUE));
        background.setCapability(Background.ALLOW_IMAGE_WRITE);
        theScene.addChild(background);

        Scene helicopter = null;
        try
        {
            ObjectFile f = new ObjectFile(ObjectFile.RESIZE);
            helicopter = f.load(helicopterPath);
        }
        catch (Exception e)
        {
            System.out.println("File loading failed:" + e);
        }

        Transform3D scaling = new Transform3D();
        scaling.setScale(1.0/4);
        Transform3D helicopterTransform = new Transform3D();
        helicopterTransform.rotY(Math.PI);
        helicopterTransform.mul(scaling);
        TransformGroup helicopterTransformGroup = new TransformGroup(helicopterTransform);
        TransformGroup sceneGroup = new TransformGroup();

        assert helicopter != null;
        BranchGroup helicopterSceneGroup = helicopter.getSceneGroup();
        helicopterSceneGroup.removeChild((Shape3D)helicopter.getNamedObjects().get("big_propeller"));
        helicopterSceneGroup.removeChild((Shape3D)helicopter.getNamedObjects().get("small_propeller"));

        sceneGroup.addChild(helicopter.getSceneGroup());


        sceneGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        helicopterTransformGroup.addChild(sceneGroup);
        theScene.addChild(helicopterTransformGroup);

        Shape3D mainBody = (Shape3D) helicopter.getNamedObjects().get("main_body_");
        setAppearance(new Color(15, 20, 15), mainBody);

        Shape3D decal = (Shape3D) helicopter.getNamedObjects().get("decal");
        setAppearance(new Color(30, 40, 30), decal);

        Shape3D glass = (Shape3D) helicopter.getNamedObjects().get("glass");
        setAppearance(new Color(180, 180, 200), glass);

        Shape3D smallPropeller = (Shape3D) helicopter.getNamedObjects().get("small_propeller");
        setAppearance(new Color(10, 10, 10), smallPropeller);

        Shape3D bigPropeller = (Shape3D) helicopter.getNamedObjects().get("big_propeller");
        setAppearance(new Color(10, 10, 10), bigPropeller);

        Shape3D otherParts = (Shape3D) helicopter.getNamedObjects().get("main_");
        setAppearance(new Color(15, 20, 15), otherParts);

        Shape3D anotherParts = (Shape3D) helicopter.getNamedObjects().get("alpha");
        setAppearance(new Color(30, 40, 30), anotherParts);

        Shape3D rocketHeadings = (Shape3D) helicopter.getNamedObjects().get("missile_gl");
        setAppearance(new Color(10, 10, 10), rocketHeadings);

        Shape3D rockets = (Shape3D) helicopter.getNamedObjects().get("missile_1");
        setAppearance(new Color(30, 40, 30), rockets);

        Transform3D transformForBigPropeller = new Transform3D();
        transformForBigPropeller.setTranslation(new Vector3f(0, 0, -0.212f));

        helicopterSceneGroup.addChild(applyRotationForShape(
                (Shape3D)helicopter.getNamedObjects().get("big_propeller"),
                transformForBigPropeller,
                320
        ));

        Transform3D transformForSmallPropeller = new Transform3D();
        transformForSmallPropeller.rotZ(Math.PI/2);
        transformForSmallPropeller.setTranslation(new Vector3f(0, 0.061f, 0.845f));

        helicopterSceneGroup.addChild(applyRotationForShape(
                (Shape3D)helicopter.getNamedObjects().get("small_propeller"),
                transformForSmallPropeller,
                200
        ));

        Transform3D transformMove = new Transform3D();
        transformMove.rotY(Math.PI/2);

        Alpha crawlAlpha = new Alpha(
                1, Alpha.INCREASING_ENABLE,0,0, 7000,0,0,0,0,0
        );
        PositionInterpolator positionInterpolator = new PositionInterpolator(
                crawlAlpha, sceneGroup, transformMove, -9.0f, 6.5f
        );

        BoundingSphere bs = new BoundingSphere(new Point3d(0,0,-600), Double.MAX_VALUE);
        positionInterpolator.setSchedulingBounds(bs);
        sceneGroup.addChild(positionInterpolator);

        theScene.compile();
        su.addBranchGraph(theScene);
    }

	//com
    private void setAppearance(Color color, Shape3D shape) {
        Appearance app = new Appearance();
        Color3f color3f = new Color3f(color);
        app.setMaterial(new Material(color3f, color3f, color3f, color3f, 150.0f));
        shape.setAppearance(app);
    }
	//com
    private Node applyRotationForShape(Shape3D shape, Transform3D transform, int rotateDuration) {
        TransformGroup transformGroup = new TransformGroup();
        transformGroup.addChild(shape.cloneTree());

        Alpha alpha = new Alpha(Integer.MAX_VALUE, Alpha.INCREASING_ENABLE,0,0, rotateDuration,
                0,0,0, 0,0);
        RotationInterpolator rotationInterpolator = new RotationInterpolator(alpha, transformGroup,
                transform, (float) Math.PI * 2, 0.0f);

        BoundingSphere bound = new BoundingSphere(new Point3d(), Double.MAX_VALUE);
        rotationInterpolator.setSchedulingBounds(bound);

        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.addChild(rotationInterpolator);

        return transformGroup;
    }

    public void addLight(SimpleUniverse su)
    {
        BranchGroup bgLight = new BranchGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        Color3f lightColour1 = new Color3f(1.0f,1.0f,1.0f);
        Vector3f lightDir1 = new Vector3f(-1.0f,0.0f,-0.5f);
        DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
        light1.setInfluencingBounds(bounds);

        bgLight.addChild(light1);
        su.addBranchGraph(bgLight);
    }


    public static void main(String[] args)
    {
        Helicopter cockroach = new Helicopter();
    }
}

