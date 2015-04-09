import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class ImageFrame extends JFrame {

	JSlider slider;
	JSlider slider2;
	JPanel panel;
	JLabel j;
	File file;
	
	private BlobDetection blobDetection;
	private BufferedImage o,b;
	private final JFileChooser chooser;
	
	public ImageFrame(int width, int height){
		
		this.setTitle("Vector Outline");
		this.setSize(width,height);
		slider = new JSlider(0, 100, 36);
		slider2 = new JSlider(3, 100, 36);
		setUpSlider();
		panel = new JPanel();
		panel.setSize(width, height);
		j = new JLabel();
		addMenu();
		
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		
	}
	public void setUpSlider(){
		 slider.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		    	  if(file != null){
						drawVectors(file);
		    	  }
		      }
		    });
		 slider2.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		    	  if(file != null){
						drawVectors(file);
		    	  }
		      }
		    });
		
		
		
		
	}
		private void addMenu(){
			JMenu fileMenu = new JMenu( "File");
			
			JMenuItem openItem = new JMenuItem("Open");
			openItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event){
					file = open();
					if(file != null){
						
						drawVectors(file);
					}
				}
			}   );
		fileMenu.add(openItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new 
				ActionListener()
				{
					public void actionPerformed( ActionEvent event){
						System.exit(0);
						
					}
			
				});
		
		fileMenu.add(exitItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		
	}
		
		
	private File open(){
		
		File file = getFile();
		if (file != null){
			
			return file;
			
		}else{
			return null;
			
		}
	}
	
	private File getFile(){
		File file = null;
		if ( chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			file = chooser.getSelectedFile();
		}
		return file;
	}
	
	
	
	public void displayBufferedImage(BufferedImage image){
		this.setLayout(new BorderLayout());
		j.setVisible(false);
		j.setIcon(new ImageIcon(image));
		j.setVisible(true);
		panel.add(j);
		this.add(slider2,  BorderLayout.SOUTH);
		this.add(slider, BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);
		repaint();
		this.validate();		
		
		this.validate();
	}
	
	private void drawVectors(File file){
		
		try{
			  b = ImageIO.read(file);
			  o = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_INT_ARGB);

			 if(b!= null){
					 int[] pixels = computePixels(b);
					 
					 blobDetection = new BlobDetection(b.getWidth(), b.getHeight());
					 blobDetection.setPosDiscrimination(false);
					 blobDetection.setThreshold((float) slider.getValue()/100.0f);
					 blobDetection.computeBlobs(pixels);

					 drawBlobsAndEdges(false, true, b);
					
			 }
			
			
		}catch( IOException exception){
			JOptionPane.showMessageDialog(this,exception);
		}
		
	}
	public int[] computePixels(BufferedImage b){
		
		int[] pixels = new int[b.getWidth() * b.getHeight()];
		for(int i = 0; i < b.getWidth(); i++){
			for(int j = 0; j < b.getHeight(); j++){
				
				// get aRGB values for each pixel and store them in a single array.
				pixels[i + j * b.getWidth()] = b.getRGB(i, j) ;
			}
			
			
		}
		
		return pixels;
	}
	void drawBlobsAndEdges(boolean drawBlobs, boolean drawEdges, BufferedImage b)
	{
		File file = new File("test.svg");
		try{
			
			// creates the file
			file.createNewFile();
			// creates a FileWriter Object
			FileWriter writer = new FileWriter(file); 
			 
	  
		
			  setUpSVGHeader(writer);	
			  setUpSVGBody(writer, 12,4,0,0,1200,400);
			  Blob blob;
			  EdgeVertex eA, eB;
			  for (int n=0 ; n<blobDetection.getBlobNb(); n++)
			  {
		
			    blob=blobDetection.getBlob(n);
				ArrayList<EdgeVertex> edgeVertices = new ArrayList<EdgeVertex>();
           
			    if (blob!=null)
			    {
		
			      // Edges
			      if (drawEdges)
			      {
			    	  /*//Drawing using g2d

			    	  Graphics2D g2d = (Graphics2D) o.createGraphics();
			    	  RenderingHints cR = new RenderingHints(
			    	             RenderingHints.KEY_COLOR_RENDERING,
			    	             RenderingHints.VALUE_COLOR_RENDER_SPEED);
			    	  g2d.setRenderingHints(cR);
			    	  
			    	  RenderingHints aA = new RenderingHints(
			    	             RenderingHints.KEY_ANTIALIASING,
			    	             RenderingHints.VALUE_ANTIALIAS_ON);
			    	  g2d.setRenderingHints(aA);
			    	  
			    	  RenderingHints r = new RenderingHints(
			    	             RenderingHints.KEY_RENDERING,
			    	             RenderingHints.VALUE_RENDER_SPEED);
			    	  g2d.setRenderingHints(r);
			    	             
			    	  Color c = new Color(255, 0, 0);
			    	  g2d.setColor(c);
			    	  BasicStroke s = new BasicStroke();
			    	  
			    	  g2d.setStroke(s);
			    	  */
			        for (int m=0;m<blob.getEdgeNb();m++)
			        {
			          
			          edgeVertices.add(blob.getEdgeVertexA(m));
			          edgeVertices.add(blob.getEdgeVertexB(m));
			          
			          /*
			          eA = blob.getEdgeVertexA(m);
			          eB = blob.getEdgeVertexB(m);
			          if (eA !=null && eB !=null)
				    	  
			             
		
			        	g2d.draw(new Line2D.Double(eA.x*b.getWidth(), eA.y*b.getHeight(), 
			        							   eB.x*b.getWidth(), eB.y*b.getHeight()));
			          
			          
		
			            
			          */
			        }
			      }
		
			      // Blobs
			      if (drawBlobs)
			      {
			    	  /*
			        strokeWeight(1);
			        stroke(255, 0, 0);
			        rect(
			        b.xMin*width, b.yMin*height, 
			        b.w*width, b.h*height
			          );
			          */
			      }
			    }
			    //remove detail from vector to the amount of vertices specified
			    removeDetail(slider2.getValue(), edgeVertices);
			    try{
			    	BezierSpline bs = new BezierSpline(edgeVertices);
			   
			    	Object[] ob = bs.GetCurveControlPoints();
			    	EdgeVertex[] c1 = (EdgeVertex[]) ob[0];
			    
			    //draw the control points and lines
			     Graphics2D g2d = (Graphics2D) o.createGraphics();
		    	  RenderingHints cR = new RenderingHints(
		    	             RenderingHints.KEY_COLOR_RENDERING,
		    	             RenderingHints.VALUE_COLOR_RENDER_SPEED);
		    	  g2d.setRenderingHints(cR);
		    	  
		    	  RenderingHints aA = new RenderingHints(
		    	             RenderingHints.KEY_ANTIALIASING,
		    	             RenderingHints.VALUE_ANTIALIAS_ON);
		    	  g2d.setRenderingHints(aA);
		    	  
		    	  RenderingHints r = new RenderingHints(
		    	             RenderingHints.KEY_RENDERING,
		    	             RenderingHints.VALUE_RENDER_SPEED);
		    	  g2d.setRenderingHints(r);
		    	             
		    	  Color c = new Color(255, 0, 0);
		    	  g2d.setColor(c);
		    	  BasicStroke s = new BasicStroke();
		    	  
		    	  g2d.setStroke(s);
		    	  for(int i = 0; i < c1.length; i++){
		    		  //System.out.println(c1[i].x + ", " + c1[i].y);
		    		  g2d.draw(new Ellipse2D.Double(c1[i].x * b.getWidth(), c1[i].y * b.getHeight(), 6, 6));
		    	  }
	    		      g2d.draw(new Ellipse2D.Double(c1[c1.length- 1].x * b.getWidth(), c1[0].y * b.getHeight(), 6, 6));
			    }catch(NullPointerException npe){}
			    //create the paths for each blob
			    writeSVGPath(writer,edgeVertices);
			  }
		
			setUpSVGEnd(writer);
			//display image
		    displayBufferedImage(o);
		}catch(IOException ioe){}
	}
	public void setUpSVGHeader(FileWriter writer){
		try{
		 writer.write("<?xml version=\"1.0\" standalone=\"no\"?>\n"
		      		+ "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"\n"
		      		+ " \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n");
		
		}catch(IOException e){}
		
	}
	public void setUpSVGBody(FileWriter writer, int width, int height, int x1, int y1, int x2, int y2 ){
		try{
		 writer.write("<svg width=\"12cm\" height=\"4cm\" viewBox=\"0 0 1200 400\"\n"
		      		+ "\t xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">\n"
		      		+ "<desc>Example polyline01 - increasingly larger bars</desc>\n"
		      		+ "<!-- Show outline of canvas using \'rect\' element -->\n");
		}catch(IOException e){}
		
	}
	public void writeSVGPath(FileWriter writer,ArrayList<EdgeVertex> e){
		
		try{
		
	      // Writes the content to the file
	      writer.write(
	    		  "<path d=\"M" + e.get(0).x * b.getWidth() + "," + e.get(0).y * b.getHeight() + "\n");
	      for(int i= 1; i < e.size() ; i++){
          	writer.write("\t\t\t  L " + e.get(i).x * b.getWidth() + "," + e.get(i).y * b.getHeight()+ " \n");
          	
          } 
	    		  
                
      	   writer.write("  z\"\n");

            writer.write("fill=\"none\" stroke=\"blue\" stroke-width=\"1\" ");
           
            writer.write("/>\n");
           
		}catch(IOException i){}
	}
	public void setUpSVGEnd(FileWriter writer){
		try{
		 writer.write("</svg>");


			
	      writer.flush();
	      writer.close();
		}catch(IOException ioe){}
	}
	public void removeDetail(int detailAmt, ArrayList<EdgeVertex> edgeVertices){
		PriorityQueue<Triangle> areaQueue = 
	            new PriorityQueue<Triangle>(edgeVertices.size(), new TriangleAreaComparator());
	    
	    while(edgeVertices.size() > detailAmt){
	    	//Triangle beginning = new Triangle(edgeVertices.get(edgeVertices.size() - 1), edgeVertices.get(0), edgeVertices.get(1), b.getWidth(), b.getHeight());
	    	//areaQueue.add(beginning);
		    for(int i = 1; i < edgeVertices.size() - 1; i++){
		    	//System.out.println(edgeVertices.get(i).x + ", " + edgeVertices.get(i).y);
		    	Triangle t = new Triangle(edgeVertices.get(i-1), edgeVertices.get(i), edgeVertices.get(i+1), b.getWidth(), b.getHeight());
		    	areaQueue.add( t);
		    	
		    }
		   // Triangle end = new Triangle(edgeVertices.get(edgeVertices.size() - 2), edgeVertices.get(edgeVertices.size() - 1), edgeVertices.get(0), b.getWidth(), b.getHeight());
		   // areaQueue.add(end);
	    	//removes the edge vertex from the triangle with the smallest area
	    	//System.out.println("area: " + areaQueue.peek().area + ", x: " + areaQueue.peek().b.x * b.getWidth() + " , y: " + areaQueue.peek().b.y * b.getHeight() + "\n");
	    	edgeVertices.remove(areaQueue.poll().b);
	    	areaQueue.clear();
		
		
	    }
	
	}
	
}
