package ui.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Created by ty on 2017/6/21.
 */

public class DrawTest {
    public static void main(String[] args)
    {
        JFrame jf = new JFrame();
//        MyPanel1 jp = new MyPanel1();
//        MyPanel2 jp = new MyPanel2();

        //在自定义Panel的外部定义一个Image绘图区
        Image im = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        //通过构造方法将缓冲缓冲区对像的引用传给自定义Panel
        MyPanel3 jp = new MyPanel3(im);
        jf.setBounds(200, 200, 500, 500);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.add(jp);
        jf.setVisible(true);

        while(true)
        {
            //不停的重绘JPanel,实现动画的效果
            jp.display();

            try
            {
                Thread.sleep(300);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 定义一个继承自JPanel的类，重写它的paint方法 *
     */
    static class MyPanel1 extends JPanel
    {
        private int x = 200;
        private int y = 200;

        public void display()
        {
            x ++;
            y ++;

            //重绘JPanel
            this.repaint();
        }

        /**
         * repaint方法会调用paint方法，并自动获得Graphics对像
         * 然后可以用该对像进行2D画图
         * 注：该方法是重写了JPanel的paint方法
         */
        public void paint(Graphics g)
        {
            //调用的super.paint(g),让父类做一些事前的工作，如刷新屏幕
            super.paint(g);
            Graphics2D g2d = (Graphics2D)g;

            g2d.setColor(Color.RED);//设置画图的颜色
            g2d.fill3DRect(x, y, 100, 100, false);//填充一个矩形
        }
    }

    /**
     * 定义一个继承自JPanel的类，重写它的paint方法 *
     */
    static class MyPanel2 extends JPanel
    {
        private int x = 200;
        private int y = 200;
        private Image image;    //图像缓冲
        private Graphics og;

        public void display()
        {
            x ++;
            y ++;

            if(og == null)
            {
                //JPanel继承自Component类，可以使用它的方法createImage创建一幅和JPanel大小相同的图形缓冲
                //然后用它Image接口的方法获得绘图对像
                image = this.createImage(this.getWidth(),this.getHeight());
                if(image != null)og = image.getGraphics();
            }

            if(og != null)
            {
                //调用的super.paint(g),让父类做一些事前的工作，如刷新屏幕
                super.paint(og);

                og.setColor(Color.RED);             //设置画图的颜色
                og.fill3DRect(x, y, 100, 100, true);//绘图
                //this.paint(this.getGraphics());
            }
            //重绘JPanel
            this.repaint();
        }

        /**
         * repaint方法会调用paint方法，并自动获得Graphics对像
         * 然后可以用该对像进行2D画图
         * 注：该方法是重写了JPanel的paint方法
         */
        public void paint(Graphics g)
        {
            g.drawImage(image, 0, 0, this);
        }
    }
    static class MyPanel3 extends JPanel
    {
        private int x = 200;
        private int y = 200;

        private Graphics g;
        private Image im ;

        //构造方法，获得外部Image对像的引用
        public MyPanel3(Image im)
        {
            if(im != null)
            {
                this.im = im;
                g = im.getGraphics();
            }
        }

        public void display()
        {
            x ++;
            y ++;
            if(g != null)
            {
                //调用的super.paint(g),让父类做一些事前的工作，如刷新屏幕
                super.paint(g);
                g.setColor(Color.RED);              //设置画图的颜色
                g.fill3DRect(x, y, 100, 100, true); //填充一个矩形
                //更新缓图
                this.repaint();
            }
        }

        /**
         * repaint方法会调用paint方法，并自动获得Graphics对像
         * 然后可以用该对像进行2D画图
         * 注：该方法是重写了JPanel的paint方法
         */
        public void paint(Graphics g)
        {
            g.drawImage(im, 0, 0, this);
        }
    }
}
