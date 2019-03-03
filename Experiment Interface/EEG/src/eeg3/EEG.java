package eeg3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

import java.io.*;
import java.util.Random;
 
class writeFile {
    public void sendTriggerToC_init() {
        InputStream ins = null;
        String label = String.valueOf(0);
        String run_exe = "trigger.exe " + label;
        String[] cmd = new String[]{ "cmd.exe", "/C", run_exe };
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            ins = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    public void sendTriggerToC(char label) {
        InputStream ins = null;
//        String label = String.valueOf(l);
        String run_exe = "trigger.exe " + label;
        String[] cmd = new String[]{ "cmd.exe", "/C", run_exe };
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            ins = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    public void write_to_file(String output, char label, String name) throws IOException {

        sendTriggerToC((char)48);
	    sendTriggerToC(label);

        File file = new File("record/"+name+"_EEG_Result.txt");
        BufferedWriter out = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(file, true)));
        out.write(output);
        out.close();
    }
  }

class initializer {
    private static int window_w = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int window_h = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static int cir_radios = Toolkit.getDefaultToolkit().getScreenSize().height / 6;
  
    public int[] initRandom_color() {
  	    int count = 0, num[] = new int[10], tmp;
        while (count < 7) {
    	    tmp = (int)(1+Math.random()*10)-1;
		    if (tmp == 0 || tmp == 1 || tmp == 2) continue;
  		    for (int i = 0; i < count; i++) {
  			    if (num[i] == tmp) break;
  			    else if (i == count-1) num[count++] = tmp;  			  
  		    }
  		    if (count == 0) num[count++] = tmp;
  	    }
  	    return num;
    }

    public int[][] initRandom_position() {
  	    int count = 0, num[][] = new int[10][2], tmp_x, tmp_y;
  	    Random rn = new Random();
  	    while (count < 10) {

  		    tmp_x = (int)(rn.nextInt(window_w - 4 * cir_radios) + 2 * cir_radios);
  		    tmp_y = (int)(1+Math.random()*((window_h-cir_radios)-cir_radios+1))-1;
  	    	for (int i = 0; i < count; i++)
  		    	if ((num[i][0]-tmp_x)*(num[i][0]-tmp_x)+(num[i][1]-tmp_y)*(num[i][1]-tmp_y) 
  			    	< cir_radios*cir_radios) break; // the five buttons do not overlay
  		    	else if (i == count-1) {
  			    	num[count][0] = tmp_x; num[count][1] = tmp_y; count++;
  			    }
     		if (count == 0) {
 	    			num[count][0] = tmp_x; num[count][1] = tmp_y; count++;
  		    }
  	    }
  	    return num;
    }

    public Color[] initColor() {
  	    Color color[] = new Color[10];
  	    color[0] = Color.black;
        color[1] = Color.gray;
        color[2] = Color.orange;
  	    color[3] = Color.yellow;
  	    color[4] = Color.green;
  	    color[5] = Color.red;
  	    color[6] = Color.blue;
  	    color[7] = Color.pink;
     	color[8] = Color.cyan;
     	color[9] = Color.magenta;
     	return color;
    }
  
    public char[] colorMapping() {
	  // map color: char to unsigned ASCII (0 - 255)
	    char colorMap[] = new char[10];
	    colorMap[0] = 100; // 'd' for black
	    colorMap[1] = 101; // 'e' for gray
	    colorMap[2] = 111; // 'o' for orange
	    colorMap[3] = 121; // 'y' for yellow
	    colorMap[4] = 103; // 'g' for green
	    colorMap[5] = 114; // 'r' for red
	    colorMap[6] = 98; // 'b' for blue
	    colorMap[7] = 112; // 'p' for pink
	    colorMap[8] = 99; // 'c' for cyan
	    colorMap[9] = 109; // 'm' for magenta
	  
	  /*
	  System.out.print("Create color mapping:\n");
	  for (int i = 0; i < 10; i++) System.out.print(colorMap[i] + "\t");
	  System.out.print("\n");
	  */
  	    return colorMap;
    }
  
}

class CircleButton extends JButton {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private char label = 0;

    public void setLabel(char l) { label = l; }

    public char getLabel_() { return label; }

    public CircleButton(String label) {
        super(label);
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);
        setContentAreaFilled(false);
    }

    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(getBackground());
        }
        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(Color.white);
        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    }

    Shape shape;

    public boolean contains(int x, int y) {
        if ((shape == null) || (!shape.getBounds().equals(getBounds()))) {
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        return shape.contains(x, y);
    }
}

class taskLab extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int window_w = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int window_h = Toolkit.getDefaultToolkit().getScreenSize().height;
  
	public taskLab() {
		super();
		this.setFont(new Font("Sans", Font.BOLD, 30));
		this.setLocation((int)(window_w / 3.7), (int)(window_h / 3.5));
		this.setSize((int)(window_w / 1.5), (int)(window_h / 3));
		this.setText("Undefined text.");
		this.setVisible(false);
	}
}

class buttonPanel extends JPanel {
	private static int window_w = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int window_h = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static int cir_w = Toolkit.getDefaultToolkit().getScreenSize().height / 6;
    private static int cir_h = Toolkit.getDefaultToolkit().getScreenSize().height / 6;
    private static int rightX_home = window_w - cir_w, Y_home = (int)(window_h * 2 / 3 - cir_h);
    private static int rightX_exit = window_w - cir_w, Y_exit = (int)(window_h * 2 / 3 + cir_h);
    public boolean pressed_home = false, pressed_exit = false;  

    public taskLab task_lab = new taskLab();
    public CircleButton button[] = new CircleButton[7];
    private static int buttonNum = 5; 
    public boolean ifClick[] = new boolean[256];
    
    private static char homeLab = 104, exitLab = 105;
  
    private String name = "Default";
    private int exe = 0, img = 1;
  
    private Color color[] = new Color[10];
    private char colorMap[] = new char[10];
    private static initializer init = new initializer();
    private static writeFile wf = new writeFile();
  
    public void set_name(String n) {
  	  name = n;
    }
  
	public void setInfoText(int task, int direction) {
  	String task_type = "";
  	if (task == img) {task_type = "IMAGE";}
  	if (task == exe) {task_type = "ACTION";}
  	
		String dir;
  	if (direction == 0) { dir = "Right-hand";}
  	else {dir = "Left-hand";}
  	
		task_lab.setText("<html> Next will be " + dir + " Group."
				+ "<br>Now enter " + task_type + " " + dir + " trials.</html>");
				//+ "<br>閻庡湱鍋ら悰娆愭交閸モ斁鏌ゅ☉鎿冨弨椤曨剟鏌嗛崹顔煎赋闂婎剦鍋傜紞瀣礂閺堢數閾傞梺顔哄妺缂嶅懘鎯冮崟顔剧闁告棏鐓夌槐婵囦繆閸屾粍韬柣顏嗩儠閿熸垝鐒︽慨鍫ユ嚈鐟併倧鎷烽敓锟�</html>");
  	task_lab.setVisible(true);
	}
	
  
  private void initButton() {
	  color = init.initColor();
	  colorMap = init.colorMapping();
      int all_ran[][] = EEG.getNextPos();
      for (int i = 0; i < buttonNum; i++) {
    	  button[i] = new CircleButton("");
          button[i].setLabel(colorMap[all_ran[i][2]]);
  		  button[i].setBackground(color[all_ran[i][2]]);
  		  button[i].setSize(cir_w, cir_h);
  		  button[i].setLocation(all_ran[i][0], all_ran[i][1]);
          
          button[i].addMouseListener(new MouseAdapter() {
      		//@Override
    		public void mousePressed(MouseEvent e) {
                try {
    			  	CircleButton b = (CircleButton)e.getSource();
					/*
                    int X_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getX()));
                    int dX = e.getX();
                    int Y_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getY()));
                    int dY = e.getY();*/
                    
                    char eventLab = b.getLabel_();
					String output = "  Press time:"+System.currentTimeMillis()+ " " + eventLab + "\n";
                    /*String output = "   time:"+System.currentTimeMillis()+
                        "ms   label:" + eventLab + "   X=" + X_ + "   Y=" 
                    	+ Y_ + "   dX=" + dX + "   dY=" + dY + "\n";*/
                    
                    System.out.print(output);
                    wf.write_to_file(output, eventLab, name);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
    		}
          });       
          button[i].addMouseListener(new MouseAdapter() {
      		//@Override
    		public void mouseReleased(MouseEvent e) {
                try {
    				CircleButton b = (CircleButton) e.getSource();
    				if (ifClick[(int)b.getLabel_()]) return;
                    b.setBackground(new Color(225, 225, 225));
                    int X_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getX()));
                    int dX = e.getX();
                    int Y_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getY()));
                    int dY = e.getY();
                    
                    char eventLab = (char) (b.getLabel_() - 32);
                    String output = "   Release time:"+System.currentTimeMillis()+
                        "ms   label:" + eventLab + "   X=" + X_ + "   Y=" 
                    	+ Y_ + "   dX=" + dX + "   dY=" + dY + "\n";

                    System.out.print(output);
                    wf.write_to_file(output, eventLab, name);
    				ifClick[(int)b.getLabel_()] = true;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
    		}
          });
 
  	}

      button[buttonNum] = new CircleButton("home");
      button[buttonNum].setFont(new Font("Sans", Font.BOLD, 35));
      button[buttonNum].setForeground(Color.WHITE);
      button[buttonNum].setBackground(Color.black);
      button[buttonNum].setSize(cir_w, cir_h);
      button[buttonNum].setLocation(rightX_home, Y_home);
      button[buttonNum].setLabel(homeLab);
      
      button[buttonNum].addMouseListener(new MouseAdapter() {
          //@Override
          public void mousePressed(MouseEvent e) {
              try {
					CircleButton b = (CircleButton)e.getSource();
                  int X_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getX()));
                  int dX = e.getX();
                  int Y_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getY()));
                  int dY = e.getY();
                  
                  char eventLab = b.getLabel_();
                  String output = "   time:"+System.currentTimeMillis()+
                      "ms   label:" + eventLab + "   X=" + X_ + "   Y=" 
                  	+ Y_ + "   dX=" + dX + "   dY=" + dY + "\n";
                  
                  System.out.print(output);
                  wf.write_to_file(output, eventLab, name);
              } catch (Exception e1) {
                  e1.printStackTrace();
              }
          }
      });        

      button[buttonNum].addMouseListener(new MouseAdapter() {
          //@Override
          public void mouseReleased(MouseEvent e) {
              try {
				  CircleButton b = (CircleButton)e.getSource();
                  int X_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getX()));
                  int dX = e.getX();
                  int Y_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getY()));
                  int dY = e.getY();
                  
                  char eventLab = (char)(b.getLabel_() - 32);
                  String output = "   time:"+System.currentTimeMillis()+
                      "ms   label:" + eventLab + "   X=" + X_ + "   Y=" 
                  	+ Y_ + "   dX=" + dX + "   dY=" + dY + "\n";
                  
                  System.out.print(output);
                  wf.write_to_file(output, eventLab, name);
                  
					button[buttonNum].setVisible(false);
                  pressed_home = true;
              } catch (Exception e1) {
                  e1.printStackTrace();
              }
          }
      });
      
      button[buttonNum + 1] = new CircleButton("exit");
      button[buttonNum + 1].setFont(new Font("Sans", Font.BOLD, 35));
      button[buttonNum + 1].setForeground(Color.WHITE);
      button[buttonNum + 1].setBackground(Color.DARK_GRAY);
      button[buttonNum + 1].setSize(cir_w, cir_h);
      button[buttonNum + 1].setLocation(rightX_exit, Y_exit);
      button[buttonNum + 1].setLabel(exitLab);
      
      button[buttonNum + 1].addMouseListener(new MouseAdapter() {
          //@Override
          public void mousePressed(MouseEvent e) {
              try {
					CircleButton b = (CircleButton)e.getSource();
                  int X_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getX()));
                  int dX = e.getX();
                  int Y_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getY()));
                  int dY = e.getY();
                  
                  char eventLab = b.getLabel_();
                  String output = "   time:"+System.currentTimeMillis()+
                      "ms   label:" + eventLab + "   X=" + X_ + "   Y=" 
                  	+ Y_ + "   dX=" + dX + "   dY=" + dY + "\n";
                  
                  System.out.print(output);
                  wf.write_to_file(output, eventLab, name);
              } catch (Exception e1) {
                  e1.printStackTrace();
              }
          }
      });        

      button[buttonNum + 1].addMouseListener(new MouseAdapter() {
          //@Override
          public void mouseReleased(MouseEvent e) {
              try {
					CircleButton b = (CircleButton)e.getSource();
                  int X_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getX()));
                  int dX = e.getX();
                  int Y_ = Integer.parseInt(new java.text.DecimalFormat("0").format(b.getLocation().getY()));
                  int dY = e.getY();
                  
                  char eventLab = (char)(b.getLabel_() - 32);
                  String output = "   time:"+System.currentTimeMillis()+
                      "ms   label:" + eventLab + "   X=" + X_ + "   Y=" 
                  	+ Y_ + "   dX=" + dX + "   dY=" + dY + "\n";
                  
                  System.out.print(output);
                  wf.write_to_file(output, eventLab, name);
                  
                  button[buttonNum + 1].setVisible(false);
                  pressed_exit = true;
              } catch (Exception e1) {
                  e1.printStackTrace();
              }
          }
      });
  }
  
  public buttonPanel() {
  	super();
  	this.setBounds(0, 0, window_w, window_h);
  	this.setBackground(new Color(225, 225, 225, 255));
  	this.setLayout(null);
  	initButton();
  	for (int i = 0; i < buttonNum + 2; i++) {
  		button[i].setVisible(false);
  		this.add(button[i]);
  	}
  	
  	task_lab.setVisible(false);
  	this.add(task_lab);
  	
  	for (int i = 0; i < 256; i++) {
  		ifClick[i] = false;
  	}
  	this.setVisible(true);
  }
  	
}


/*
class taskPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static int window_w = Toolkit.getDefaultToolkit().getScreenSize().width;
  private static int window_h = Toolkit.getDefaultToolkit().getScreenSize().height;
  private int exe = 0, img = 1;

	private JLabel task_lab = new JLabel();
	
	public taskPanel() {
		super();
  	this.setBounds(0, 0, window_w, window_h);
  	this.setLayout(new GridBagLayout());
  	
  	task_lab = new JLabel();
		task_lab.setFont(new Font("Sans", Font.BOLD, 20));
  	task_lab.setVisible(true);
  	
  	GridBagConstraints constraints = new GridBagConstraints();
  	constraints.anchor = GridBagConstraints.CENTER;
  	constraints.insets = new Insets(10, 10, 10, 10);
  	constraints.gridx = 0;
  	constraints.gridy = 0;
  	this.add(task_lab, constraints);
  	
  	this.setVisible(true);
	}
	
	public void setInfoText(int task, String text_) {
  	String task_type = "";
  	if (task == img) {task_type = "Movement Imagination";}
  	if (task == exe) {task_type = "Movement Execution";}
		task_lab.setText("<html> Next task: " + task_type 
				+ "<br> Plz use your " + text_ + " hand.</html>");
  	task_lab.setVisible(true);
	}
}
*/

class welcomePanel extends JPanel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int window_w = Toolkit.getDefaultToolkit().getScreenSize().width;
  private static int window_h = Toolkit.getDefaultToolkit().getScreenSize().height;
  private static int name_w = window_w / 3, name_h = window_h / 5;
  private static int input_name_w = name_w / 2, input_name_h = name_h / 4;

	private JLabel msg = new JLabel();
	private JTextField name_input = new JTextField();
		
	public welcomePanel() {
		super();
  	this.setBounds(0, 0, window_w, window_h);
  	this.setLayout(new GridBagLayout());
  	this.requestFocusInWindow();
  	
  	msg = new JLabel();
  	msg.setFont(new Font("Sans", Font.PLAIN, 20));
  	msg.setText("<html> Welcome to Movement Analysis Experiment!<br>"
  			//+ "闁革负鍔岄悿鍕殽瀹�鍐畺缂佸顑勯懙鎴︽晬瀹�鍐惧殲婵炲鍔嶉崜鐗堢閵夈倗鐟撳ù婊冾儔閵嗗秹鏁嶉敓锟�<br><br>"
  			//+ "1. 閻犲洤鍢查崢娑㈡偨閵婏箑顤侀柟绋挎处鐎垫粍绋夌�ｎ喚鎷ㄩ柣鐐插枦缁辨繈鐛捄鐑樿含闊洤鍟撮崳閿嬵渶濡粯娈跺☉鎾愁槺椤宕ユ惔鈥虫櫃闁哄鍎崇槐鎴︽晬閿燂拷<br>"
  			//+ "2. 閻犲洤鍢插搴㈡媴鐠恒劍鏆忛柛娆掓珪婢ф粓鏁嶇仦鍊熷珯濞戞挻鏌ㄨぐ褔鎮介妸銈囶伇闁哄秴婀卞ù澶愬触瀹�锟藉▓鎴﹀箥鐎ｎ偄鐦归悗鐟版湰閸ㄦ岸鏁嶉敓锟�<br>"
  			//+ "3. 閻犲洨鍏樻导鈺呭礂瀹ュ牓鐓╁ù锝嗘尭閸欑偓绂掗弽顓炲姤濞达絽绉跺▓鎴﹀礉閵娿倗绋婇柨娑樿嫰椤┭囨晬濮樿鲸韬柣顏嗐�嬬槐婵嬪箮閺嶎剙鎮柨娑虫嫹<br>"
  			//+ "4. 閻犲洩娓圭粩鎾儎缂堢姷绠介柟闀愯兌濠у倿鎯堝☉姘导鐎殿噯鎷烽柨娑樼焷椤鐥懗顖涘劙闁革负鍔岄惈鍡涚嵁閺囨氨鐟愰柨娑虫嫹<br>"
  			//+ "5. 閻犲洩娓圭换姘跺箰娴ｄ粙鐓╁ù锝嗘尰閺備線寮堕幘鍛濞戞挸绉烽々锔炬瑜嶇槐鍫曞Υ閿燂拷 <br><br><br>"
  			+ "Please enter your name:</html>");
  	msg.setVisible(true);
  	
  	GridBagConstraints constraints = new GridBagConstraints();
  	constraints.anchor = GridBagConstraints.CENTER;
  	constraints.insets = new Insets(10, 10, 10, 10);
  	constraints.gridx = 0;
  	constraints.gridy = 0;
  	this.add(msg, constraints);
  	
  	name_input = new JTextField();
	    name_input.setFont(new Font("Sans", Font.BOLD, 30));
	    name_input.setVisible(true);
	    
  	GridBagConstraints constraintsTF = new GridBagConstraints();
  	constraintsTF.anchor = GridBagConstraints.CENTER;
  	constraintsTF.insets  = new Insets(10, 30, 30, 30);
  	constraintsTF.gridx = 0;
  	constraintsTF.gridy = 2;
  	constraintsTF.ipadx = input_name_w * 2;
  	constraintsTF.ipady = input_name_h / 3;
	    this.add(name_input, constraintsTF);
	    
  	this.revalidate();
  	this.repaint();
  	this.setVisible(true);
  	    	
	}
	
	public String get_name() {
		return name_input.getText();
		}
}

class endPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int window_w = Toolkit.getDefaultToolkit().getScreenSize().width;
  private static int window_h = Toolkit.getDefaultToolkit().getScreenSize().height;
  
	public endPanel() {
		super();
  	this.setBounds(0, 0, window_w, window_h);
  	this.setLayout(new GridBagLayout());
  	
  	JLabel end_text = new JLabel("<html><br><br><br>Thank you for your participation.</html>");
      end_text.setFont(new Font("Sans", Font.BOLD, 60));
      
  	GridBagConstraints constraints = new GridBagConstraints();
  	constraints.anchor = GridBagConstraints.NORTH;
  	constraints.gridx = 0;
  	constraints.gridy = 0;
      this.add(end_text, constraints);
      
      this.setVisible(true);
	}
}

class Window extends JFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int window_w = Toolkit.getDefaultToolkit().getScreenSize().width;
  private static int window_h = Toolkit.getDefaultToolkit().getScreenSize().height;
  
	public Window() {
		super();
		this.setSize(window_w, window_h);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		this.setTitle("Welcome to EEG experiment");
	    this.setResizable(false);
		this.setVisible(false);
	}
}

class NewWindow {

  private static int buttonNum = 5; 
  private static int window_w = Toolkit.getDefaultToolkit().getScreenSize().width;
  private static int window_h = Toolkit.getDefaultToolkit().getScreenSize().height;
  private static int cir_w = Toolkit.getDefaultToolkit().getScreenSize().height / 6;
  private static int cir_h = Toolkit.getDefaultToolkit().getScreenSize().height / 6;
 private static int rightX_home = window_w - cir_w, leftX_home = (int)(window_w - 10 * 1.07 * cir_w), Y_home = (int)(window_h * 2 / 3 - cir_h);
  private static int rightX_exit = window_w - cir_w, leftX_exit = (int)(window_w - 10 * 1.07 * cir_w), Y_exit = (int)(window_h * 2 / 3 + cir_h);

  private int exe = 0, img = 1, cnt = 0;
  private static int img_turn = 5, exe_turn = 10, total_round = 10;
  private static char BALL_SHOW = 85, GRAY_SCR = 90;
  
  private long start_time = -1;

  private JFrame clock_win = new JFrame("Break Time");
  private JLabel clock_text = new JLabel();
  private static int clock_w = Toolkit.getDefaultToolkit().getScreenSize().width / 3;
  private static int clock_h = Toolkit.getDefaultToolkit().getScreenSize().height / 4;

  private static initializer init = new initializer();
  private static writeFile wf = new writeFile();
  private String name;
  private boolean setupStatus = false, trialStatus = false;
  
  private buttonPanel buttonPane = new buttonPanel();
  private welcomePanel welcomePane = new welcomePanel();   
  private endPanel endPane = new endPanel();
  private Window NewWin = new Window();
  
  private void init_time() {
      clock_win.setVisible(false);
      clock_win.setLocation(window_w / 2 - clock_w / 2, window_h / 2 - clock_h / 2);
      clock_win.setSize(clock_w, clock_h);
      clock_win.setLayout(new FlowLayout(FlowLayout.CENTER));
      JPanel myJPanel = new JPanel();
		clock_text.setFont(new Font("Sans", Font.BOLD, 20));
      myJPanel.add(clock_text);
      clock_win.getContentPane().add(myJPanel);
      clock_win.setAlwaysOnTop(true);
  }

  private void show_time(long t_) {
      clock_win.setVisible(true);
      while (t_ >= 0) {
          long hour = t_ / 3600;
          long minute = (t_ - hour * 3600) / 60;
          long seconds = t_ - hour * 3600 - minute * 60;
          clock_text.setText("<html>Have a rest now."
          		//+ "<br>Now rest for 5 minites. "
          		//+ "<br>闁告艾鏈鍌涚▕閻旀椿鍤炴繛澶堝妽閸撶増绋夊鍫矗閻熸瑱濡囬～顐ゆ媼閹屾У闁挎稑鐭傛导鈺呭礂瀹ュ懌浠堝┑鍌涱殘浜涘ù锝呯Р閿熸枻鎷�"
          		+ "<br><br>The experiment will begin in: "
              +hour+":"+minute+":"+seconds);
          try {
              Thread.sleep(1000);
          } catch (Exception e) {
              e.printStackTrace();
          }
          t_--;
      }
      clock_win.setVisible(false);
  }
  

  private void afterAllClick(int direction) {
  	// direction: 0 = right, 1 = left
      int all_ran[][] = EEG.getNextPos();
      Color color[] = init.initColor();
      char colorMap[] = init.colorMapping();
      
      for (int i = 0; i < buttonNum; i++) {
          buttonPane.button[i].setLabel(colorMap[all_ran[i][2]]);
          buttonPane.button[i].setBackground(color[all_ran[i][2]]);
          buttonPane.button[i].setLocation(all_ran[i][0], all_ran[i][1]);
      }
      for (int i = 0; i < 256; i++) buttonPane.ifClick[i] = false;
      if (direction == 0) {
      	buttonPane.button[buttonNum].setLocation(rightX_home, Y_home);
      	buttonPane.button[buttonNum + 1].setLocation(rightX_exit, Y_exit);
      } else {
      	buttonPane.button[buttonNum].setLocation(leftX_home, Y_home);
      	buttonPane.button[buttonNum + 1].setLocation(leftX_exit, Y_exit);
      }
  }

  private boolean checkAllClick() {
      try {
    	  int countClicked = 0;
          for (int i = 0; i < 256; i++) {
              if (buttonPane.ifClick[i]) countClicked++;        	  
          }
          if (countClicked != buttonNum) return false;
		  cnt++;
			
      } catch (Exception e) {
          e.printStackTrace();
      }
      return true;
  }
		
  public void exeTurn(int exe_cnt, int direction) {
  	try {
  		Random rn = new Random();
  		
      	if (cnt == 0) {
          	buttonPane.setInfoText(exe, direction);
          	buttonPane.task_lab.setVisible(true);
          	Thread.sleep(5 * 1000 + rn.nextInt(1) * 1000);
              buttonPane.task_lab.setVisible(false);
              
      		Thread.sleep(2000 + rn.nextInt(1) * 1000);
      		}
          else {
              Thread.sleep(2 * 1000 + rn.nextInt(1) * 1000);
              for (int i = 0; i < buttonNum; i++) {
              	buttonPane.button[i].setVisible(false);
              }
              
              String output = "   time:" + System.currentTimeMillis() + "ms   "
                  + "label:" + GRAY_SCR  + "\n";
              System.out.print(output);
              wf.write_to_file(output, GRAY_SCR, name);
              
              if (exe_cnt == 0) {
              	buttonPane.setInfoText(exe, direction);
              	buttonPane.task_lab.setVisible(true);
              	Thread.sleep(5 * 1000 + rn.nextInt(1) * 1000);
                  buttonPane.task_lab.setVisible(false);
          	}

              Thread.sleep(2 * 1000 + rn.nextInt(1) * 1000);
              afterAllClick(direction);
          }
          
          for (int i = 0; i < buttonNum + 1; i++) {buttonPane.button[i].setVisible(true);}
          buttonPane.button[buttonNum + 1].setVisible(false);
          
      	char ballShow = BALL_SHOW;
      	if (direction == 1) ballShow = (char)(BALL_SHOW + 1);
          String output = "   time:" + System.currentTimeMillis() +
              "ms   label:" + ballShow + "\n";
          System.out.print(output);
          wf.write_to_file(output, ballShow, name);
          
          while (!buttonPane.pressed_home)
              Thread.sleep(1);
          buttonPane.pressed_home = false;

          while (!checkAllClick())
              Thread.sleep(1); 
          
  	} catch (Exception e) {
  		e.printStackTrace();
  	}
  }
  
  public void imgTurn(int img_cnt, int direction) {
  	try{
  		Random rn = new Random();
          
          Thread.sleep(2 * 1000 + rn.nextInt(1) * 1000);
          for (int i = 0; i < buttonNum; i++) {buttonPane.button[i].setVisible(false);}
          String output = name + "   time:" + System.currentTimeMillis() +
              "ms   label:" + GRAY_SCR  + "\n";
          System.out.print(output);
          wf.write_to_file(output, GRAY_SCR, name);
          
          if (img_cnt == 0) {
          	buttonPane.setInfoText(img, direction);
          	buttonPane.task_lab.setVisible(true);
          	Thread.sleep(5 * 1000 + rn.nextInt(1) * 1000);
              buttonPane.task_lab.setVisible(false);
      	}

          Thread.sleep(2 * 1000 + rn.nextInt(1) * 1000);
          afterAllClick(direction);
          
          for (int i = 0; i <= buttonNum + 1; i++) {buttonPane.button[i].setVisible(true);}
          
          char ballShow = 0;
          if (direction == 0) { ballShow = (char)(BALL_SHOW + 2);}
          else {ballShow = (char)(BALL_SHOW + 3);}       
          String output2 = "   time:" + System.currentTimeMillis() +
              "ms   label:" + ballShow + "\n";
          System.out.print(output2);
          wf.write_to_file(output2, ballShow, name);

          while (!buttonPane.pressed_home)
              Thread.sleep(1);
          buttonPane.pressed_home = false;

          while (!buttonPane.pressed_exit)
              Thread.sleep(1);
          buttonPane.pressed_exit = false;
          for (int i = 0; i < buttonNum;  i++) {
          	buttonPane.button[i].setBackground(new Color(225, 225, 225));
          	}
          
      } catch (Exception e) {
  		e.printStackTrace();
  	}
  }    
  
	public void setTrialStatus(boolean new_status) {
		trialStatus = new_status;
	}
  
	public boolean getTrialStatus() {
		return trialStatus;
	}
  
  public void kernel() {
      try {
			start_time = System.currentTimeMillis();
			
          while (start_time == -1)
              Thread.sleep(1);
			wf.sendTriggerToC((char)48);
			wf.sendTriggerToC('1');
          init_time();
          
          int total_turn = img_turn + exe_turn;

          for (int direction = 0; direction < 2; direction++) { 
          	
              for (int round_ = 1; round_ <= total_round; round_++) {
              	
              	int img_cnt = 0, exe_cnt = 0;
                  for (int turn_ = 1; turn_ <= total_turn; turn_++) {
                  	
                  	if (turn_ <= exe_turn) {
                  		exeTurn(exe_cnt, direction);
                  		exe_cnt++;
                  		}
                    	else {
                    		imgTurn(img_cnt, direction); 
                    		img_cnt++;
                    		}
                  	
                  }
                  
                  if (round_ < total_round) {
                  	
                      show_time(20); // 60
                      start_time = System.currentTimeMillis();
                      
                  }

              }
              
              if (direction == 0) {
              	
                  show_time(5 * 60); // 5 * 60
                  start_time = System.currentTimeMillis();
                  
              }
              
          }
          
          setTrialStatus(true);

      }
      catch (Exception e) {
          e.printStackTrace();
      }
  }  
  
	public void setSetupStatus(boolean new_status) {
		setupStatus = new_status;
	}
  
	public boolean getSetupStatus() {
		return setupStatus;
	}
  
	public void setup() {
		
	    int name_w = window_w / 3, name_h = window_h / 5;
	    int submit_name_w = name_w / 4, submit_name_h = name_h / 5;

	    JButton name_submit = new JButton("submit");
	    name_submit.setFont(new Font("Sans", Font.BOLD, 20));
	    name_submit.setVisible(true);
	    
  	GridBagConstraints constraintsButt = new GridBagConstraints();
  	constraintsButt.anchor = GridBagConstraints.CENTER;
  	constraintsButt.insets  = new Insets(10, 30, 30, 30);
  	constraintsButt.gridx = 0;
  	constraintsButt.gridy = 3;
  	constraintsButt.ipadx = submit_name_w;
  	constraintsButt.ipady = submit_name_h / 4;
  	
	    welcomePane.add(name_submit, constraintsButt);
	    welcomePane.revalidate();
	    welcomePane.repaint();
	    	    
	    name_submit.addActionListener(new ActionListener() { 
	        public void actionPerformed(ActionEvent e) {
	        	try{
		        	name = new String();
		            name = welcomePane.get_name();
		            buttonPane.set_name(name);
		            setSetupStatus(true);
		            
					welcomePane.setVisible(false);
					NewWin.getContentPane().removeAll();
					
					NewWin.getContentPane().add(buttonPane);
					NewWin.getContentPane().revalidate();
					NewWin.getContentPane().repaint();
					           
	        	} catch (Exception event) {
	        		event.printStackTrace();
	        	}

	        }
	    });
	    
	    welcomePane.setVisible(true);
	    
	}
	
	public void endTrial() {
		NewWin.getContentPane().removeAll();
		NewWin.getContentPane().add(endPane);
		NewWin.getContentPane().revalidate();
		NewWin.getContentPane().repaint();
	}
	
	public NewWindow() throws IOException {
		super();
		NewWin.getContentPane().add(welcomePane);
		NewWin.setVisible(true);
	}
}


public class EEG {

  private static int[][][] pos_store = new int[10000][10][3];
  private static int pos_idx = -1;
  private static initializer init = new initializer();

  public static int[][] getNextPos() {
      if (pos_idx+1 >= 10000) pos_idx = 0;
      else pos_idx++;
      return pos_store[pos_idx];
  }

  private static void generate(int num) throws IOException {
      File file = new File("record/Random_Position.txt");
      BufferedWriter out = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(file, false)));
      String output = "";
      int pos[][] = new int[10][2];
      int col[] = new int[10];
      System.out.println("generating random position ...");
      for (int i = 0; i < num; i++) {
          pos = init.initRandom_position();
          col = init.initRandom_color();
          output = "";
          for (int j = 0; j < 10; j++)
              output += "("+String.valueOf(pos[j][0])+","+String.valueOf(pos[j][1])+","+String.valueOf(col[j])+")";
          out.write(output+"\n");
      }
      System.out.println(num + " positions have been generated.");
      out.close();
  }

  private static void load(int num) throws IOException {
      File file = new File("record/Random_Position.txt");
      BufferedReader br = new BufferedReader(new FileReader(file));
      String thisLine = "";
      int line = 0;
      while ((thisLine = br.readLine()) != null && line < num) {
          int idx = 0, cir_idx = 0;
          while (idx < thisLine.length()) {
              idx++;
              String X_ = "", Y_ = "", C_ = "";
              while (thisLine.charAt(idx) != ',') X_ += thisLine.charAt(idx++);
              idx++;
              while (thisLine.charAt(idx) != ',') Y_ += thisLine.charAt(idx++);
              idx++;
              while (thisLine.charAt(idx) != ')') C_ += thisLine.charAt(idx++);
              idx++;
              pos_store[line][cir_idx][0] = Integer.parseInt(X_);
              pos_store[line][cir_idx][1] = Integer.parseInt(Y_);
              pos_store[line][cir_idx][2] = Integer.parseInt(C_);
              cir_idx++;
          }
          line++;
      }
  }

  public static void main(String args[]) throws Exception {
      generate(10000);
      load(10000);
      
      NewWindow myWindow = new NewWindow();
      
		myWindow.setup();
		while (!myWindow.getSetupStatus()) {
			Thread.sleep(1);
		}
		
		myWindow.kernel();
		while (!myWindow.getTrialStatus()) {
			Thread.sleep(1);
		}
		
		myWindow.endTrial();
  }

}

/*

Appendix# 1 (color-label table)

LABEL COLOR
0   'reset'
1   'submit'
2   'home' - black
3   yellow
4   green
5   red
6   blue
7   pink
8   cyan
9   magenta

*/
