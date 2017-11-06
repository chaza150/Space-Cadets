import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpiroFrame extends JFrame implements MouseListener{
	
	SpiroPanel canvas;
	double spiroRadius1;
	double spiroRadius2;
	double spiroOffset;
	
	public SpiroFrame() {
		canvas = new SpiroPanel(this.getWidth(), this.getHeight());
		setLayout(new BorderLayout());
		JSlider outerRadius = new JSlider(JSlider.VERTICAL, 0, 50, 50);
		JSlider innerRadius = new JSlider(JSlider.VERTICAL, 0, 50, 25);
		JSlider offset = new JSlider(JSlider.VERTICAL, 0, 20, 6);
		
		outerRadius.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if(!source.getValueIsAdjusting()) {
					spiroRadius2 = (int) source.getValue();
				}
			}
		});
		
		innerRadius.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if(!source.getValueIsAdjusting()) {
					spiroRadius1 = (int) source.getValue();
				}
			}
		});
		
		offset.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if(!source.getValueIsAdjusting()) {
					spiroOffset = (int) source.getValue();
				}
			}
		});
		
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new FlowLayout());
		
		sliderPanel.add(offset);
		sliderPanel.add(innerRadius);
		sliderPanel.add(outerRadius);
		
		add(sliderPanel, "East");
	}
	
	public void startup() {
		this.setBackground(Color.black);
		this.addMouseListener(this);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		canvas.draw(new Hypercycloid(e.getX(), e.getY(), spiroRadius1, spiroRadius2, spiroOffset), this.getGraphics());
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
}
