package foto1;

import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class foto5 extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage img_O;
	private BufferedImage img_Seg;
	private BufferedImage img_Seg_c;
    private JPanel contentPane;
    private JTextField txtNSeg;
	/**
	 * Launch the application.
	 */
    
    public static void makeGray(BufferedImage img)
    {
    	
    	
        for (int x = 0; x < img.getWidth(); ++x)
        {
        	for (int y = 0; y < img.getHeight(); ++y)
            {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                // Normalize and gamma correct:
                double rr = Math.pow(r / 255.0, 2.2);
                double gg = Math.pow(g / 255.0, 2.2);
                double bb = Math.pow(b / 255.0, 2.2);

                // Calculate luminance:
                double lum = (0.2126 * rr + 0.7152 * gg + 0.0722 * bb);

                // Gamma compand and rescale to byte range:
                int grayLevel = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel; 
                img.setRGB(x,y, gray);
            }
        }   
    }
    
    public BufferedImage processamento()
    {
    	BufferedImage img_c = img_Seg_c;
    	
    	makeGray(img_Seg_c);
    	
    	ArrayList<Integer> kkk = new ArrayList<Integer>(); 
    	
    	int x = img_Seg_c.getWidth(), y = img_Seg_c.getHeight();
    	for(int i = 0; i< x; i++)
    	{
    		for(int j = 0; j<y; j++)
    		{
    			if(!kkk.contains(img_Seg_c.getRGB(i, j)))
    			{
    				kkk.add(img_Seg_c.getRGB(i, j));
    			}
    		}
    	}
    	
    	Dictionary<Integer,int[]> geek = new Hashtable<Integer, int[]>();
    	
    	for(int valor : kkk)
    	{
    		int count = 0;
    		int suma = 0;
    		int sumr = 0;
    		int sumg = 0;
    		int sumb =0;
    		int[] temp = new int[4];
    		for(int i = 0; i< x; i++)
        	{
        		for(int j = 0; j<y; j++)
        		{
        			if(valor == img_Seg_c.getRGB(i, j))
        			{
        				count++;
        				
        				int p = img_O.getRGB(i, j);
        				int ialpha;
        				int iRed;
        	            int iGreen;
        	            int iBlue;
        	            
        				ialpha = (p>>24) & 0xff;
                        iRed = (p>>16) & 0xff;
                        iGreen = (p >>8) & 0xff;
                        iBlue = p  & 0xff;
                        
        				suma += ialpha;
        	            sumr += iRed;
        				sumg += iGreen;
        				sumb += iBlue;
        			}
        		}
        	}
    		temp[0] = suma/count;
    		temp[1] = sumr/count;
    		temp[2] = sumg/count;
    		temp[3] = sumb/count;
    		geek.put(valor, temp);
    		
    	}
    	for(int valor : kkk)
    	{
    		int[] temp = null;
    		for(int i = 0; i< x; i++)
        	{
        		for(int j = 0; j<y; j++)
        		{
        			if(valor == img_Seg_c.getRGB(i, j))
        			{
        				temp = geek.get(valor);
        				int p = (temp[0]<<24) | (temp[1]<<16) |(temp[2]<<8) |temp[3];
        				img_c.setRGB(i, j, p);
        			}
        		}
        	}
    	}
    
		return img_c;
    	
    }

	private File getImage() {

		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		int returnValue = jfc.showOpenDialog(null);
		// int returnValue = jfc.showSaveDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION)  {
			return jfc.getSelectedFile();
		}
		else
		{
			return null;
		}

	}
	/*
	public void carrega_imagem(JLabel lblImg, BufferedImage img)
	{
		File selectedFile = getImage();
		ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
		try {
			img = ImageIO.read(selectedFile.getAbsoluteFile());	
		} catch (IOException ex) {
			Logger.getLogger(foto5.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		int y = lblImg.getWidth();
		int x = lblImg.getHeight();
		
		Image image = icon.getImage();
		Image newImg = image.getScaledInstance(x, y,  java.awt.Image.SCALE_SMOOTH);
		
		icon = new ImageIcon(newImg);
		
		lblImg.setIcon(icon);
	}
	*/
	public void carrega_imagem2(JLabel lblImg, BufferedImage img, int flag)
	{
		File selectedFile = getImage();
		try
		{
		    img = ImageIO.read(selectedFile.getAbsoluteFile());
		    
		    if(flag == 2)
		    {
		    	img_Seg_c = ImageIO.read(selectedFile.getAbsoluteFile());
		    }
		    else
		    {
		    	img_O = ImageIO.read(selectedFile.getAbsoluteFile());
		    }
		}
		catch (IOException e)
		{
		    String workingDir = System.getProperty("user.dir");
		    System.out.println("Current working directory : " + workingDir);
		    e.printStackTrace();
		}	
		int y = lblImg.getWidth();
		int x = lblImg.getHeight();
		
		ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
		
		Image image = icon.getImage();
		Image newImg = image.getScaledInstance(x, y,  java.awt.Image.SCALE_SMOOTH);
		
		icon = new ImageIcon(newImg);
		
		lblImg.setIcon(icon);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					foto5 frame = new foto5();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public foto5() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		//////////////////////////////////////////////////Labels
		JLabel lblImg = new JLabel("");
		lblImg.setBounds(10, 105, 539, 557);
		contentPane.add(lblImg);
		
		JLabel lblSeg = new JLabel("");
		lblSeg.setBounds(715, 105, 539, 557);
		contentPane.add(lblSeg);
		
//////////////////////////////////////////////////botões de carregar imagem
		
		JButton btnCarregarImg = new JButton("Carregar Imagem");
		btnCarregarImg.setBounds(191, 71, 127, 23);
		btnCarregarImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//carrega_imagem(lblImg, img_Seg);
				carrega_imagem2(lblImg,img_O, 1);
			}
		});
		contentPane.add(btnCarregarImg);		
		
		JButton btnNewButton_1 = new JButton("Carregar segmentação");
		btnNewButton_1.setBounds(943, 71, 143, 23);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//carrega_imagem(lblSeg,img_O);
				carrega_imagem2(lblSeg,img_Seg, 2);
		
			}});
		contentPane.add(btnNewButton_1);
				
		
//////////////////////////////////////////////////botão pra iniciar o processamento
		
		JButton btn_svg = new JButton("processar");
		btn_svg.setBounds(627, 11, 127, 23);
		btn_svg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BufferedImage img_F = processamento();
				
				process jframe = new process();
				
				ImageIcon icon_F = new ImageIcon(img_F);
				
				jframe.lblSeg.setIcon(icon_F);
				
				
				jframe.setVisible(true);
				File selectedFileFile = new File("out.png");
				try {
					ImageIO.write(img_F, "png",selectedFileFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		contentPane.add(btn_svg);
		
		txtNSeg = new JTextField();
		txtNSeg.setBounds(1121, 31, 86, 20);
		contentPane.add(txtNSeg);
		txtNSeg.setColumns(10);
		
		JButton btnNewButton = new JButton("Criar segmenta\u00E7\u00E3o");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int K = Integer.parseInt(txtNSeg.getText());
				img_Seg_c = KMeans.run(img_O, K);
				
				ImageIcon icon_F = new ImageIcon(img_Seg_c);
				
				lblSeg.setIcon(icon_F);
				
				
			}
		});
		btnNewButton.setBounds(943, 30, 143, 23);
		contentPane.add(btnNewButton);
			
	}
}
