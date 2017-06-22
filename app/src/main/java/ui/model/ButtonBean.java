/*
 * Copyright 2007-2008 Sun Microsystems, Inc.  All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ui.model;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.Icon;

import ui.ButtonDemo;

/**
 * Wrapper class which encapsulates a GUI component to be displayed
 * as a SwingSet3 demo.
 *
 * @author Amy Fowler
 */
public class ButtonBean {



    private Component component;

    private Exception failException;
    public enum State {UNINITIALIZED, INITIALIZING, INITIALIZED, RUNNING, STOPPED, FAILED}

    private State state;
    private static final String IMAGE_EXTENSIONS[] = {".gif", ".png", ".jpg"};

    private PropertyChangeSupport mPropertyChangeSupport;

    public ButtonBean() {
        mPropertyChangeSupport = new PropertyChangeSupport(this);
    }

    public Component createDemoComponent() {
        Component component = null;

        component = new ButtonDemo();
        return component;
    }
    void setDemoComponent(Component component) {


    }
    /**
     * 初始化
     */
    private void init() {
        setState(State.INITIALIZING);
//        try {
////            Method initMethod = JPanelClass.getMethod("init", (Class[])null);
////            initMethod.invoke(component, (Object[])null);
//        } catch (NoSuchMethodException nsme) {
//            // okay, no init method exists
//        } catch (IllegalAccessException iae) {
////            SwingSet3.logger.log(Level.SEVERE, "unable to init demo: "+ JPanelClass.getName(), iae);
//            failException = iae;
//            setState(State.FAILED);
//        } catch (java.lang.reflect.InvocationTargetException ite) {
////            SwingSet3.logger.log(Level.SEVERE, "init method failed for demo: "+ JPanelClass.getName(), ite);
//            failException = ite;
//            setState(State.FAILED);
//        } catch (NullPointerException npe) {
////            SwingSet3.logger.log(Level.SEVERE, "init method called before demo was instantiated: "
////                    + JPanelClass.getName(), npe);
//            failException = npe;
//            setState(State.FAILED);
//        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        mPropertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        mPropertyChangeSupport.removePropertyChangeListener(pcl);
    }

    private String name;
    private Icon icon;
    private String desc; // used for tooltips

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void startInitializing() {
        setState(State.INITIALIZING);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(
                "\nname:" + name
                        + "\nicon:" + icon
                        + "\nstate:" + state
                        + "\ndesc:" + desc).toString();
    }
}
