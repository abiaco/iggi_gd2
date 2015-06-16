package battle.gui;

import battle.BattleController;
import battle.SimpleBattle;
import battle.controllers.Human.WASDController;
import battle.controllers.RotateAndShoot;
import battle.controllers.mmmcts.MMMCTS;
import battle.controllers.mmmcts.tools.ElapsedCpuTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by mmoros on 16/06/2015.
 */
public class MainMenu extends JFrame {

    private static Object lock = new Object();
    private static JFrame frame = new JFrame();

    public MainMenu()
    {
        initUI();
    }

    private void initUI() {
        JButton quitButton = new JButton("Quit");

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        JButton startMCTSVSMan = new JButton("MCTS vs Human");

        startMCTSVSMan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Thread t = new Thread() {
                    public void run() {
                         StartGame(new MMMCTS(), new WASDController());

                    }
                };
                t.start();

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent arg0) {
                        synchronized (lock) {
                            frame.setVisible(false);
                            lock.notify();
                        }
                    }
                });
            }
        });

        createLayout(startMCTSVSMan,quitButton);

        setTitle("Simple example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private static void StartGame(BattleController p1, BattleController p2)
    {
        SimpleBattle battle = new SimpleBattle(true, true);

        frame = battle.windowFrame;

        ElapsedCpuTimer ecp = new ElapsedCpuTimer();
        battle.playGame(p1, p2);
        System.out.println("Time to run " + ecp.elapsedMillis());
    }

    private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GridLayout gl = new GridLayout(2,2);
        pane.setLayout(gl);

        //gl.setAutoCreateContainerGaps(true);

        for(JComponent comp : arg)
        {
            pane.add(comp);
        }

    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                MainMenu ex = new MainMenu();
                ex.setVisible(true);
            }
        });
    }
}
