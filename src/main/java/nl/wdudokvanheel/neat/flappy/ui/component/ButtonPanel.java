package nl.wdudokvanheel.neat.flappy.ui.component;

import nl.wdudokvanheel.neat.flappy.ui.NeatFlappyWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

public class ButtonPanel extends JPanel{
	JButton speedBtn = new JButton("");
	JButton pauseBtn = new JButton("");
	JButton noScreen = new JButton("");
	NeatFlappyWindow ui;

	public ButtonPanel(NeatFlappyWindow ui){
		this.ui = ui;
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		speedBtn.setAction(new Action(){
			@Override
			public Object getValue(String key){
				return null;
			}

			@Override
			public void putValue(String key, Object value){

			}

			@Override
			public void setEnabled(boolean b){

			}

			@Override
			public boolean isEnabled(){
				return true;
			}

			@Override
			public void addPropertyChangeListener(PropertyChangeListener listener){

			}

			@Override
			public void removePropertyChangeListener(PropertyChangeListener listener){

			}

			@Override
			public void actionPerformed(ActionEvent e){
				if(ui.speed == 1){
					ui.speed = 2;
					speedBtn.setText("Speed: 2x");
				}
				else if(ui.speed == 2){
					ui.speed = 4;
					speedBtn.setText("Speed: 4x");
				}
				else if(ui.speed == 4){
					ui.speed = 8;
					speedBtn.setText("Speed: 8x");
				}
				else if(ui.speed == 8){
					ui.speed = 1;
					speedBtn.setText("Speed: 1x");
				}
				ui.pack();
			}

		});

		pauseBtn.setAction(new Action(){
			@Override
			public Object getValue(String key){
				return null;
			}

			@Override
			public void putValue(String key, Object value){

			}

			@Override
			public void setEnabled(boolean b){

			}

			@Override
			public boolean isEnabled(){
				return true;
			}

			@Override
			public void addPropertyChangeListener(PropertyChangeListener listener){

			}

			@Override
			public void removePropertyChangeListener(PropertyChangeListener listener){

			}

			@Override
			public void actionPerformed(ActionEvent e){
				ui.paused = !ui.paused;
				if(ui.paused){
					pauseBtn.setText("Resume");
				}
				else {
					pauseBtn.setText("Pause");
				}
				ui.pack();
			}
		});
		noScreen.setAction(new Action(){
			@Override
			public Object getValue(String key){
				return null;
			}

			@Override
			public void putValue(String key, Object value){

			}

			@Override
			public void setEnabled(boolean b){

			}

			@Override
			public boolean isEnabled(){
				return true;
			}

			@Override
			public void addPropertyChangeListener(PropertyChangeListener listener){

			}

			@Override
			public void removePropertyChangeListener(PropertyChangeListener listener){

			}

			@Override
			public void actionPerformed(ActionEvent e){
				ui.noGfx = !ui.noGfx;
				if(ui.noGfx){
					noScreen.setText("Show gfx");
				}
				else {
					noScreen.setText("Hide gfx");
				}
			}
		});

		add(pauseBtn);
		add(speedBtn);
		add(noScreen);

		speedBtn.setText("Speed: 1x");
		pauseBtn.setText("Pause");
		noScreen.setText("No Screen");
	}
}
