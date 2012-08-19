/*
 * Copyright 2012 Benjamin Glatzel <benjamin.glatzel@me.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.rendering.gui.components;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector2f;

import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.logic.manager.AudioManager;
import org.terasology.rendering.gui.framework.UIDisplayContainer;
import org.terasology.rendering.gui.framework.UIDisplayElement;
import org.terasology.rendering.gui.framework.events.ChangedListener;
import org.terasology.rendering.gui.framework.events.MouseButtonListener;
import org.terasology.rendering.gui.framework.events.MouseMoveListener;

/**
 * A simple graphical button usable for creating user interface.
 *
 * @author Benjamin Glatzel <benjamin.glatzel@me.com>
 * @author Marcel Lehwald <marcel.lehwald@googlemail.com>
 */
public class UIButton extends UIDisplayContainer {

    private final UIText _label;
    
	public enum eButtonType {NORMAL, TOGGLE};
    private boolean _toggleState = false;
    private eButtonType _buttonType;
    
    private final Map<String, Vector2f[]> states = new HashMap<String, Vector2f[]>();

    /**
     * Create a simple button, where 2 types are possible. The normal button and the toggle button.
     * @param size The size of the button.
     * @param buttonType The type of the button which can be normal or toggle.
     */
    public UIButton(Vector2f size, eButtonType buttonType) {
        setSize(size);
        
        _buttonType = buttonType;
       
        //default button
        setTexture("engine:gui_menu");
        setNormalState(new Vector2f(0f, 0f), new Vector2f(256f, 30f));
        setHoverState(new Vector2f(0f, 30f), new Vector2f(256f, 30f));
        setPressedState(new Vector2f(0f, 60f), new Vector2f(256f, 30f));
        
        //default state
        setBackgroundImageSource(states.get("normal")[0], states.get("normal")[1]);
        setBackgroundImageTarget(new Vector2f(0f, 0f), size);

        addMouseMoveListener(new MouseMoveListener() {	
			@Override
			public void leave(UIDisplayElement element) {
				if (_buttonType == eButtonType.TOGGLE) {
					if (_toggleState) {
						setBackgroundImageSource(states.get("pressed")[0], states.get("pressed")[1]);
					} else {
						setBackgroundImageSource(states.get("normal")[0], states.get("normal")[1]);
					}
				}
				else {
					setBackgroundImageSource(states.get("normal")[0], states.get("normal")[1]);
				}
			}
			
			@Override
			public void hover(UIDisplayElement element) {

			}
			
			@Override
			public void enter(UIDisplayElement element) {
	            AudioManager.play(new AssetUri(AssetType.SOUND, "engine:click"), 1.0f);
	            if (_buttonType == eButtonType.NORMAL) {
	            	setBackgroundImageSource(states.get("hover")[0], states.get("hover")[1]);
	            }
			}

			@Override
			public void move(UIDisplayElement element) {

			}
		});
        
        addMouseButtonListener(new MouseButtonListener() {					
			@Override
			public void up(UIDisplayElement element, int button, boolean intersect) {
				if (_buttonType == eButtonType.NORMAL) {
					setBackgroundImageSource(states.get("normal")[0], states.get("normal")[1]);
				}
			}
			
			@Override
			public void down(UIDisplayElement element, int button, boolean intersect) {
				if (intersect) {
					if (_buttonType == eButtonType.TOGGLE) {
						if (_toggleState) {
							setBackgroundImageSource(states.get("normal")[0], states.get("normal")[1]);
							_toggleState = false;
						} else {
							setBackgroundImageSource(states.get("pressed")[0], states.get("pressed")[1]);
							_toggleState = true;
						}
					} else {
						setBackgroundImageSource(states.get("pressed")[0], states.get("pressed")[1]);
					}
				}
			}
			
			@Override
			public void wheel(UIDisplayElement element, int wheel, boolean intersect) {

			}
		});
        
        _label = new UIText("Untitled");
        _label.setVisible(true);
        _label.addChangedListener(new ChangedListener() {
			@Override
			public void changed(UIDisplayElement element) {
				layout();
			}
		});
        
        addDisplayElement(_label);
    }

    @Override
    public void layout() {
    	super.layout();
    	
    	if (_label != null) {
    		_label.setPosition(new Vector2f(getSize().x / 2 - getLabel().getTextWidth() / 2, getSize().y / 2 - getLabel().getTextHeight() / 2));
    	}
    }

    public UIText getLabel() {
        return _label;
    }
    
    /**
     * Set the texture of the button. Use setNormalTexture, setHoverTexture and setPressedTexture to configure the texture origin and size of the different states.
     * @param texture The texture to load by the AssetManager.
     */
    public void setTexture(String texture) {
        setBackgroundImage(texture);
    }
    
    /**
     * Set the normal states texture origin and size. Set the texture by using setTexture.
     * @param origin The origin.
     * @param size The size.
     */
    public void setNormalState(Vector2f origin, Vector2f size) {
    	states.remove("normal");
    	states.put("normal", new Vector2f[] {origin, size});
    	
    	//set default state
        setBackgroundImageSource(states.get("normal")[0], states.get("normal")[1]);
    }
    
    /**
     * Set the hover states texture origin and size. Set the texture by using setTexture. In toggle mode this texture will be ignored.
     * @param origin The origin.
     * @param size The size.
     */
    public void setHoverState(Vector2f origin, Vector2f size) {
    	states.remove("hover");
    	states.put("hover", new Vector2f[] {origin, size});
    	
    	//set default state
        setBackgroundImageSource(states.get("normal")[0], states.get("normal")[1]);
    }
    
    /**
     * Set the pressed states texture origin and size. Set the texture by using setTexture.
     * @param origin The origin.
     * @param size The size.
     */
    public void setPressedState(Vector2f origin, Vector2f size) {
    	states.remove("pressed");
    	states.put("pressed", new Vector2f[] {origin, size});
    	
    	//set default state
        setBackgroundImageSource(states.get("normal")[0], states.get("normal")[1]);
    }
    
    public boolean getToggleState() {
		return _toggleState;
	}
    
    /**
     * Set the state of the toggle button. Only has an affect if the button was created as an toggle button.
     * @param state True to set the pressed state.
     */
    public void setToggleState(boolean state) {
    	_toggleState = state;
    	
		if (_toggleState) {
			setBackgroundImageSource(states.get("pressed")[0], states.get("pressed")[1]);
		} else {
			setBackgroundImageSource(states.get("normal")[0], states.get("normal")[1]);
		}
    }
}
