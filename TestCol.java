
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.util.Random;
//
/**
 *
 * @author Teuku Hashrul NPM : 2016730067
 */
public class TestCol extends SimpleApplication implements ActionListener, AnalogListener {

    private Geometry chara, obstacle, terrain;

    private int score; // nampung scorenyaaa

    private int highScore; //nampung highscore nya

    private CharacterControl charaControl; 
    private RigidBodyControl obstacleControl, terrainControl;

    private BitmapFont defaultFont; // font yang dipake
    
    /*
       attribut buat nanti wadah munculin textnya 
    fpsScore : score sementara 
    highSCore : buat nampung high scorenya
    pressStart : buat panduan  klik start , sama reset game
    */
    private BitmapText fpsScoreText, pressStart, fpsHighScoreText; 
    

    // boolean buat nge toggle gamenya start
    private boolean starting = false;

    private BulletAppState bas;

    public static void main(String[] args) {
        TestCol app = new TestCol();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //Setting camera
        flyCam.setMoveSpeed(100);
        cam.setLocation(new Vector3f(-0.5828292f, 10.728814f, -64.55383f));
        cam.setRotation(new Quaternion(0.28176963f, -0.005618209f, 0.0016497344f, 0.9594642f));

        // Setting bullet app state
        bas = new BulletAppState();
        bas.setDebugEnabled(true);
        stateManager.attach(bas);

        // dismissing fps counter and stat 
        setDisplayFps(false);
        setDisplayStatView(false);

        //initialization score , high score and start / reset state
        defaultFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        pressStart = new BitmapText(defaultFont, false);
        fpsScoreText = new BitmapText(defaultFont, false);
        fpsHighScoreText = new BitmapText(defaultFont, false);
        loadText(fpsScoreText, "Current score : 0", defaultFont, 0, 3, 10);
        loadText(pressStart, "Press v to Start ", defaultFont, 0, 5, 10);
        loadText(fpsHighScoreText, "HighScore : 0 ", defaultFont, 0, 7, 9);

        //attaching all the object into rootNode
        rootNode.attachChild(makeFloor());
        rootNode.attachChild(makeChara());
        rootNode.attachChild(makeObstacleDarat());

        //  initialization key method
        initKeys();

        //intialize score and highScore to 0 
        highScore = 0;
        score = 0;
    }

    /*
     Custom method to add mapping name , key input and add those to the listener 
    
     */
    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Print", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_G));

        inputManager.addMapping("Start", new KeyTrigger(KeyInput.KEY_V));

        // Add the names to the action listener.
        inputManager.addListener(this, "Jump", "Start", "Print", "Reset"); // add it into action listener
        inputManager.addListener(this, "Left", "Right"); // add it into analog listener 

    }

    /*
        Custom method to create custom Box
        1 create box
        2 create geometry and add the box
        3 set the box location
        4 set material
        5 setting material color
        6 add material into geometry
        
        7 create the collision shape and scale it as big as the geometry using getWorldScale
        8 Create characterControl and add the collision shape
        9 add the character control into the geometry
        10  set the jumpspeed , fallspeed , and gravity
        11 add the character control into the physicsSpace
        12 return geomtetry 
     */
    protected Geometry makeChara() {
        Box b = new Box(1, 1, 1);
        chara = new Geometry("Character", b);
        chara.setLocalTranslation(0.0f, -2.8000004f, -40.61673f);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Red);
        chara.setMaterial(mat1);
        BoxCollisionShape box = new BoxCollisionShape(chara.getWorldScale());

        charaControl = new CharacterControl(box, speed);

        chara.addControl(charaControl);
        charaControl.setJumpSpeed(30);
        charaControl.setFallSpeed(100);
        charaControl.setGravity(80);

        bas.getPhysicsSpace().add(charaControl);

        return chara;
    }

    /**
     * Method buat bikin obstaclenya 
     * collision shapenya gue bikin tipe boxcollisionshape biar sesuai kotaknya parameternya kan vector3f ,jadi
     * gue ambil v3nya si geometrynya biar ukuran collision shape nya sama geometry nya sesuai
     * 
     * pokonya konsep kasih physic itu 
     * 1. bikin collision shape 
     * 2. bikin rigid body control (karena terrain jadi pake rigid body bukan character control) trus parameternya 
     * ( parameter 1 : collision shape nya yang dibikn di no 1 , parameter 2 : massa nya (massa 0 : statis contohnya floor/ terrain kalo yang character , obstacle massa nya jangan 0)
     * 3. si geometry / node / spatial add control si rigid  bodynya
     * 4. masukin si rigid bodynya ke  bullet app state(bas)
     * @return 
     */
    protected Geometry makeObstacleDarat() {
        Box b = new Box(1, 1, 1);
        obstacle = new Geometry("Obstacle", b);
        obstacle.setLocalTranslation(0.0f, -2.8000004f, 40.61673f);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Brown);
        obstacle.setMaterial(mat1);
        BoxCollisionShape box = new BoxCollisionShape(obstacle.getWorldScale());
        obstacleControl = new RigidBodyControl(box, 0.0f);
        obstacle.addControl(obstacleControl);
        bas.getPhysicsSpace().add(obstacleControl);
        return obstacle;
    }

    /*
    ini methode buat generate posisi 3 lane nya
        kanan : -6.0915997f tengah : 2.0915997f kiri : 7.0915997f
     nanti pas di simple update pas box nya z nya udah -100 gue bakal setphysic location buat balikin si box
    nah di method ini , gue generate posisi x nya nanti masukin ke parameter x nya si set physic location
    */
    public float obsDaratPositionGenerator() {
        Random rand = new Random();
        float kiri = 7.0915997f;
        float tengah = 0.0f;
        float kanan = -6.0915997f;
        int num = 1 + rand.nextInt(4);
        if (num == 1) {
            return kiri;
        } else if (num == 2) {
            return kanan;
        } else {
            return tengah;
        }
    }

    /**
     * Method buat bikin floornya 
     * collision shapenya gue bikin tipe boxcollisionshape biar sesuai kotaknya parameternya kan vector3f ,jadi
     * gue ambil v3nya si geometrynya biar ukuran collision shape nya sama geometry nya sesuai
     * 
     * pokonya konsep kasih physic itu 
     * 1. bikin collision shape 
     * 2. bikin rigid body control (karena terrain jadi pake rigid body bukan character control) trus parameternya 
     * ( parameter 1 : collision shape nya yang dibikn di no 1 , parameter 2 : massa nya (massa 0 : statis contohnya floor/ terrain kalo yang character , obstacle massa nya jangan 0)
     * 3. si geometry / node / spatial add control si rigid  bodynya
     * 4. masukin si rigid bodynya ke  bullet app state(bas)
     * @return 
     */
    protected Geometry makeFloor() {
        Box b = new Box(10, .2f, 150);
        Vector3f v3 = new Vector3f(10, .2f, 200);
        terrain = new Geometry("the Floor", b);
        terrain.setLocalTranslation(0, -4, -5);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Gray);
        terrain.setMaterial(mat1);
        BoxCollisionShape box = new BoxCollisionShape(v3); // 1.
        terrainControl = new RigidBodyControl(box, 0.0f);  // 2.
        terrain.addControl(terrainControl); // 3.
        bas.getPhysicsSpace().add(terrainControl); //4
        return terrain;
    }

    /*
     * 
     * @param name
     * @param isPressed
     * @param tpf
    
        override method interface action listener 
    print :  itu buat ngeprint posisi si character itu cuma buat test doang 
    
    start : dia bakal ngetoggle attribut boolean starting
    
    reset : buat ngereset posisi box karakter yang udah kalah , sama ngereset score , reset posisi obstacle
     sama ngetoggle attribut starting jadi false lagi seperti pas awal baru di run programnya
    
     */
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (starting) {
            if (name.equals("Print") && !isPressed) {
                Vector3f obj = charaControl.getPhysicsLocation();
                System.out.println(obj.x + "f ," + obj.y + "f ," + obj.z + "f");
            }
            if (name.equals("Jump") && !isPressed) {
                charaControl.jump();
            }
        }
        if (name.equals("Start") && !isPressed) {
            starting = !starting;
            System.out.println("ok");
            obstacleControl.setMass(0.0f);
            pressStart.setText("");
        }
        if (name.equals("Reset")) {
            score = 0;
            obstacleControl.setPhysicsLocation(new Vector3f(0.0f, -2.8000004f, 40.61673f));

            charaControl.setPhysicsLocation(new Vector3f(0.0f, -2.8000004f, -40.61673f));

            loadText(fpsScoreText, "Current score : 0", defaultFont, 0, 3, 10);
            loadText(pressStart, "Press v to Start ", defaultFont, 0, 5, 10);

        }
    }

    @Override
    /**
     * 
     */
    public void simpleUpdate(float tpf) {
        /*
         nah if pertama tuh tadi gamenya ga bakal mulai sebelum player klik v buat start 
         kalo belom di toggle boolean nya ga bakal mulai simple updatenya
        */
        if (starting) {
            
            /*
             ini buat ngejalanin obstacle musuh ke arah karakter 
             INGAT ! kalo jalanin obstacle yang udah dikasih physics jangan pake setlocal translation 
             tapi pake setPhysics location supaya collision shape nya juga gerak 
            */
            Vector3f obsPos = obstacleControl.getPhysicsLocation(); 
            obstacleControl.setPhysicsLocation(obsPos.set(obsPos.x, obsPos.y, obsPos.z - tpf * 30)); // 2
            
            
            /**
             *  ini buat ngecek kalo obstacle posisinya udah dibelakang kamera dibalikin ke depan lagi
             * nah cuman buat ngerandom posisi lane nya gue pas ngeset x nya pake method obsDaratPositionGenerator
             */
            
            if (obsPos.z < -100) {  //Reset obstacle
                obstacleControl.setPhysicsLocation(new Vector3f(obsDaratPositionGenerator(), -2.8000004f, 40.61673f));
            }

            /*
             BACA - BACA - BACA ! 
             ini gue jelasin konsep state kalah nya 
            
             jadi kalo obstacle jalan terus menuju gue kan otomatis karakter gue nabrak , jadi gue konsepnya
             kalo karakter gue kedorong dikit aja terhadap znya maka gue pastiin kalah atau si y nya udah - 3 itu kalo jatoh kiri kanan
             
            nah kalo kalah si boolean starting tadi yang nge toggle semuanya gue bikin false lagi biar selesai game nya
            */
            Vector3f charaPos = charaControl.getPhysicsLocation();
            //nabrak                   // jatoh
            if (charaPos.z < -40.62f || charaPos.y < -3.0f) {
                starting = !starting;
                System.out.println("LOSE !");
                pressStart.setText("You Lose , press G to Reset ! ");
                if (score >= highScore) {
                    highScore = score;
                    fpsHighScoreText.setText("High Score : " + highScore);
                }

            }

            score++; // nge counter scorenya 
            System.out.println("score : " + score);
           
            // abis di counter di set ke wadah scorenya secara continously
            fpsScoreText.setText("Current score: " + score);

        }

    }

    
    /*
     ini override analoglistener buat kiri kanan
    */
    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (starting) {
            if (name.equals("Left")) {
                Vector3f obsPos = charaControl.getPhysicsLocation();
                charaControl.setPhysicsLocation(obsPos.set(obsPos.x + tpf * 10, obsPos.y, obsPos.z));
            }
            if (name.equals("Right")) {
                Vector3f obsPos = charaControl.getPhysicsLocation();
                charaControl.setPhysicsLocation(obsPos.set(obsPos.x - tpf * 10, obsPos.y, obsPos.z));
            }
        }
    }

    /**
     * Sets up a BitmapText to be displayed
     *
     * @param txt the Bitmap Text
     * @param text the
     * @param font the font of the text
     * @param x
     * @param y
     * @param z
     */
    private void loadText(BitmapText txt, String text, BitmapFont font, float x, float y, float z) {
        txt.setSize(font.getCharSet().getRenderedSize());
        txt.setLocalTranslation(txt.getLineWidth() * x, txt.getLineHeight() * y, z);
        txt.setText(text);
        guiNode.attachChild(txt);
    }

}
