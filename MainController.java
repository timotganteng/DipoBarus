
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 * UPDATE 7 NOVEMBER 2017
 * summon character, summon floor tapi cuma geometry doang dari box
 *
 * @author DipoBarus Team Game Developer
 */
public class MainController extends SimpleApplication {

    private Spatial chara;

    public static void main(String[] args) {
        MainController game = new MainController();
        game.start(); // start the apllication
    }

    public void simpleInitApp() {
        flyCam.setMoveSpeed(100); // merubah flycam speed
        cam.setLocation(new Vector3f(10, speed, -2));

        rootNode.attachChild(makeFloor()); // memasukan lantai
        rootNode.attachChild(makeChara()); // memasukan character

        // We add light so we see the scene
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
        
         viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    }

    /**
     * Make plain floor , with unshaded material
     *
     * @return
     */
    public Geometry makeFloor() {
        Box b = new Box(15, .2f, 200);
        Geometry floor = new Geometry("the Floor ", b);
        floor.setLocalTranslation(0, -4, 5);
       
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Gray);
        floor.setMaterial(mat1);
        return floor;
    }

    public Spatial makeChara() {
        chara = assetManager.loadModel("Models/Oto/Oto.mesh.j3o");
        chara.setLocalScale(1f);
        chara.setLocalTranslation(0,1f,150f);
        chara.rotate(0, 3, 0);
        return chara;
    }

}
