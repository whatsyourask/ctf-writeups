/*     */ package alien;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.net.Socket;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ 
/*     */ public class Starter
/*     */   extends JFrame
/*     */   implements ActionListener
/*     */ {
/*  29 */   Container container = getContentPane();
/*  30 */   JLabel userLabel = new JLabel("USERNAME");
/*  31 */   JLabel passwordLabel = new JLabel("PASSWORD");
/*  32 */   JTextField userTextField = new JTextField();
/*  33 */   JPasswordField passwordField = new JPasswordField();
/*  34 */   JButton loginButton = new JButton("LOGIN");
/*  35 */   JButton resetButton = new JButton("RESET");
/*  36 */   JButton uploadButton = new JButton("UPLOAD");
/*  37 */   JButton viewButton = new JButton("VIEW");
/*  38 */   JButton infoButton = new JButton("INFO");
/*  39 */   JCheckBox showPassword = new JCheckBox("Show Password");
/*  40 */   JTextArea field = new JTextArea();
/*     */   
/*     */   static Token token;
/*     */   static String role;
/*     */   ObjectOutputStream os;
/*     */   ObjectInputStream is;
/*     */   Socket socket;
/*     */   
/*     */   public Starter() {
/*  49 */     setLayoutManager();
/*  50 */     setLocationAndSize();
/*  51 */     addComponentsToContainer();
/*  52 */     addActionEvent();
/*  53 */     this.showPassword.setBackground(Color.GRAY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*  59 */     Starter frame = new Starter();
/*  60 */     frame.setTitle("WARZONE 3 | Login Form");
/*  61 */     frame.setVisible(true);
/*  62 */     frame.setBounds(10, 10, 370, 600);
/*  63 */     frame.setDefaultCloseOperation(3);
/*  64 */     frame.setResizable(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*  75 */     if (e.getSource() == this.loginButton) {
/*     */       
/*  77 */       String username = this.userTextField.getText();
/*  78 */       String password = this.passwordField.getText();
/*     */ 
/*     */       
/*     */       try {
/*  82 */         this.socket = new Socket("192.168.88.222", 4444);
/*  83 */         this.os = new ObjectOutputStream(this.socket.getOutputStream());
/*     */         
/*  85 */         RE login = new RE();
/*  86 */         login.setToken(null);
/*  87 */         login.setOption("LOGIN");
/*  88 */         login.setCmd(null);
/*  89 */         login.setValue(String.valueOf(username) + "@" + password);
/*  90 */         this.os.writeObject(login);
/*     */         
/*  92 */         this.is = new ObjectInputStream(this.socket.getInputStream());
/*  93 */         RE response = (RE)this.is.readObject();
/*  94 */         token = response.getToken();
/*  95 */         role = "astronaut";
/*  96 */         this.os.close();
/*  97 */         this.socket.close();
/*     */         
/*  99 */         if (response.getValue().equals("TRUE")) {
/*     */           
/* 101 */           dashboard();
/*     */         } else {
/*     */           
/* 104 */           JOptionPane.showMessageDialog(this, "Invalid Username or Password");
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 109 */       catch (IOException|ClassNotFoundException e1) {
/*     */         
/* 111 */         e1.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/* 115 */     if (e.getSource() == this.resetButton) {
/* 116 */       this.userTextField.setText("");
/* 117 */       this.passwordField.setText("");
/*     */     } 
/*     */     
/* 120 */     if (e.getSource() == this.showPassword) {
/* 121 */       if (this.showPassword.isSelected()) {
/* 122 */         //this.passwordField.setEchoChar();
/*     */       } else {
/* 124 */         this.passwordField.setEchoChar('*');
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 129 */     if (e.getSource() == this.viewButton)
/*     */     {
/* 131 */       if (role.equals("researcher")) {
/*     */         
/* 133 */         JOptionPane.showMessageDialog(this, "Permission Denied");
/* 134 */       } else if (role.equals("astronaut")) {
/*     */ 
/*     */         
/*     */         try {
/* 138 */           this.socket = new Socket("192.168.88.222", 4444);
/*     */           
/* 140 */           this.os = new ObjectOutputStream(this.socket.getOutputStream());
/*     */           
/* 142 */           RE list = new RE();
/* 143 */           token.setRole(role);
/* 144 */           list.setToken(token);
/* 145 */           list.setOption("VIEW");
/* 146 */           list.setCmd("LIST");
/* 147 */           list.setValue(null);
/*     */           
/* 149 */           this.os.writeObject(list);
/*     */ 
/*     */           
/* 152 */           this.is = new ObjectInputStream(this.socket.getInputStream());
/* 153 */           RE response = (RE)this.is.readObject();
/* 154 */           this.os.close();
/* 155 */           this.socket.close();
/* 156 */           reportList(response.getValue());
/*     */         }
/* 158 */         catch (IOException e1) {
/*     */           
/* 160 */           e1.printStackTrace();
/* 161 */         } catch (ClassNotFoundException e1) {
/*     */           
/* 163 */           e1.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     if (e.getSource() == this.uploadButton)
/*     */     {
/* 174 */       JOptionPane.showMessageDialog(this, "Has not been implemented");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLayoutManager() {
/* 180 */     this.container.setLayout((LayoutManager)null);
/* 181 */     this.container.setBackground(Color.GRAY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocationAndSize() {
/* 186 */     this.userLabel.setBounds(50, 150, 100, 30);
/* 187 */     this.passwordLabel.setBounds(50, 220, 100, 30);
/* 188 */     this.userTextField.setBounds(150, 150, 150, 30);
/* 189 */     this.passwordField.setBounds(150, 220, 150, 30);
/* 190 */     this.showPassword.setBounds(150, 250, 150, 30);
/* 191 */     this.loginButton.setBounds(50, 300, 100, 30);
/* 192 */     this.resetButton.setBounds(200, 300, 100, 30);
/* 193 */     this.uploadButton.setBounds(50, 300, 100, 30);
/* 194 */     this.viewButton.setBounds(200, 300, 100, 30);
/* 195 */     this.field.setBounds(20, 20, 320, 50);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComponentsToContainer() {
/* 201 */     this.container.add(this.userLabel);
/* 202 */     this.container.add(this.passwordLabel);
/* 203 */     this.container.add(this.userTextField);
/* 204 */     this.container.add(this.passwordField);
/* 205 */     this.container.add(this.showPassword);
/* 206 */     this.container.add(this.loginButton);
/* 207 */     this.container.add(this.resetButton);
/*     */   }
/*     */   
/*     */   public void addActionEvent() {
/* 211 */     this.loginButton.addActionListener(this);
/* 212 */     this.resetButton.addActionListener(this);
/* 213 */     this.showPassword.addActionListener(this);
/* 214 */     this.uploadButton.addActionListener(this);
/* 215 */     this.viewButton.addActionListener(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dashboard() {
/* 220 */     this.field.setText("This is a secret researching system.");
/* 221 */     this.field.append("\nHere you can submit or view reports about aliens behavior");
/* 222 */     this.field.setEditable(false);
/* 223 */     this.container.removeAll();
/* 224 */     setLayoutManager();
/* 225 */     this.container.add(this.uploadButton);
/* 226 */     this.container.add(this.viewButton);
/* 227 */     this.container.add(this.field);
/* 228 */     this.container.setLayout(new FlowLayout());
/* 229 */     this.container.repaint();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reportList(String value) {
/* 235 */     JFrame view = new JFrame("View Reports");
/* 236 */     GridLayout list = new GridLayout(2, 2);
/* 237 */     Container containerLIst = view.getContentPane();
/* 238 */     containerLIst.setLayout(list);
/* 239 */     containerLIst.setBackground(Color.GRAY);
/*     */     
/* 241 */     String[] files = value.split("@"); byte b; int i; String[] arrayOfString1;
/* 242 */     for (i = (arrayOfString1 = files).length, b = 0; b < i; ) { final String f = arrayOfString1[b];
/*     */       
/* 244 */       if (f.contains(".txt")) {
/* 245 */         JButton name = new JButton(f);
/* 246 */         name.addActionListener(new ActionListener()
/*     */             {
/*     */               public void actionPerformed(ActionEvent e) {
/*     */                 try {
/* 250 */                   Starter.this.socket = new Socket("192.168.88.222", 4444);
/*     */                   
/* 252 */                   Starter.this.os = new ObjectOutputStream(Starter.this.socket.getOutputStream());
/*     */                   
/* 254 */                   RE list = new RE();
/*     */                   
/* 256 */                   list.setToken(Starter.token);
/* 257 */                   list.setOption("VIEW");
/* 258 */                   list.setValue("VALUE");
/* 259 */                   list.setCmd("tail -5 " + f);
/*     */ 
/*     */                   
/* 262 */                   Starter.this.os.writeObject(list);
/*     */ 
/*     */                   
/* 265 */                   Starter.this.is = new ObjectInputStream(Starter.this.socket.getInputStream());
/* 266 */                   RE response = (RE)Starter.this.is.readObject();
/* 267 */                   Starter.this.os.close();
/* 268 */                   Starter.this.socket.close();
/* 269 */                   Starter.this.reportValue(response.getValue());
/*     */                 }
/* 271 */                 catch (IOException e1) {
/*     */                   
/* 273 */                   e1.printStackTrace();
/* 274 */                 } catch (ClassNotFoundException e1) {
/*     */                   
/* 276 */                   e1.printStackTrace();
/*     */                 } 
/*     */               }
/*     */             });
/*     */ 
/*     */ 
/*     */         
/* 283 */         containerLIst.add(name);
/*     */       } 
/*     */       b++; }
/*     */     
/* 287 */     view.setVisible(true);
/* 288 */     view.setBounds(10, 10, 370, 600);
/* 289 */     view.setDefaultCloseOperation(3);
/* 290 */     view.setResizable(true);
/* 291 */     view.show();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reportValue(String value) {
/* 298 */     JTextArea output = new JTextArea(" ");
/* 299 */     String[] text = value.split("@"); byte b; int i; String[] arrayOfString1;
/* 300 */     for (i = (arrayOfString1 = text).length, b = 0; b < i; ) { String word = arrayOfString1[b];
/*     */       
/* 302 */       output.append(String.valueOf(word) + " ");
/* 303 */       output.setEditable(false);
/*     */       
/*     */       b++; }
/*     */ 
/*     */     
/* 308 */     output.setColumns(30);
/* 309 */     output.setLineWrap(true);
/* 310 */     output.setWrapStyleWord(true);
/* 311 */     output.setSize((output.getPreferredSize()).width, 1);
/* 312 */     JOptionPane.showMessageDialog(null, new JScrollPane(output), "Report", 
/* 313 */         2);
/*     */   }
/*     */ }


/* Location:              /root/repos/CTF-writeups/VulnHub/Warzone: 3/ftp/alienclient.jar!/alien/Starter.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */
