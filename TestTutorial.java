
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;



/**
 *
 * @author Teuku Hashrul
 */
public class TestTutorial extends SimpleApplication{
    public static void main(String[] args) {
        TestTutorial app = new TestTutorial();
        app.start();
    }
    
    /*
     attribut bertipe ( spatial , baca di java doc jmonkey enginenya ) berbentuk ninja
    */
    protected Spatial ninja;
    
    
    Boolean isRunning = true;
    
    /*
     * method yang wajib meng di override karena kelas ini mengextends simpleApplication
       sebagai inisialisasi awal dari game nya
     */
    @Override
    public void simpleInitApp(){
      // Meng- import ninja dari model yang sudah disediakan jmonkey engine
      this.ninja = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
       ninja.scale(0.05f , 0.02f , 0.05f); // mengubah ukuran ninja sesuai scale
       ninja.rotate(0.0f , -3.0f , 0.0f); // men set dengan memutar dengan sumbu y
       ninja.setLocalTranslation(0.0f, -5.0f, -2.0f); // menggeser 
       rootNode.attachChild(ninja); // harus di attach
       
       //you must add a light to make the model visible
       DirectionalLight sun = new DirectionalLight();
       sun.setDirection(new Vector3f(-0.1f , -0.7f , -1.0f));
       rootNode.addLight(sun);
       initKeys();
       
    }
    
    private void initKeys(){
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE ) , new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(analogListener, "Left" , "Right" , "Rotate");
    }
    
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if(name.equals("Pause") && !isPressed){
                isRunning = !isRunning;
            }
        }
    };
    
    private AnalogListener analogListener = new AnalogListener(){
        public void onAnalog(String name , float value , float tpf){
            if(isRunning){
                if(name.equals("Rotate")){
                    ninja.rotate(0 , value*speed , 0);
                }
                if(name.equals("Right")){
                    Vector3f v = ninja.getLocalTranslation();
                    ninja.setLocalTranslation(v.x + value*speed*3, v.y, v.z);
                }
                if(name.equals("Left")){
                    Vector3f v = ninja.getLocalTranslation();
                    ninja.setLocalTranslation(v.x - value*speed*3, v.y, v.z);
                }
            }else{
                System.out.println("Press p to unpause.");
            }
        }
    };
            
    
//    public void simpleUpdate(float tpf){
//        this.ninja.rotate(0.0f , tpf ,0.f);
//    }
}
