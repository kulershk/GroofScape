import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javafx.stage.Screen;
import javax.swing.JFrame;

public class Main extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 1280;
	public static final int HEIGHT = WIDTH/12*6;
	public static final int SCALE = 1;
	public static final String NAME = "Kulers test game";

	private JFrame frame;

	public boolean running = false;
	public int tickCount = 0;

	private Screen screen;
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private int[] colours = new int[6*6*6];

	public Main(){
		setMinimumSize(new Dimension(WIDTH* SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH* SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH* SCALE, HEIGHT * SCALE));

		frame = new JFrame(NAME);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(this, BorderLayout.CENTER);
		frame.pack();

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void tick(){
		tickCount++;
	}

	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		running = false;
	}

	public static void main(String[] args){
		new Main().start();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;

		int ticks=0;
		int frames=0;

		long lastTimer=System.currentTimeMillis();
		double delta=0;

		init();

		while(running){
			long now = System.nanoTime();
			delta +=(now-lastTime)/nsPerTick;
			lastTime=now;
			boolean shouldRender=true;

			while(delta >= 1){
				ticks++;
				tick();
				delta-=1;
				shouldRender=true;
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(shouldRender){
				frames++;
				render();
			}
			if(System.currentTimeMillis()-lastTimer >= 1000){
				lastTimer+=1000;
				System.out.println(ticks+","+frames);
				frames =0;
				ticks=0;
			}
		}

	}

	public void init(){

	}

	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs==null){
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		g.setColor(Color.RED);
		g.drawLine(10,10,100,100);

		g.dispose();
		bs.show();
	}

}
