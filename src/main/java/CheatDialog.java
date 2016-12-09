/*
 * CheatDialog.java
 *
 * Created on 29 octobre 2006, 20:15
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author Krishna3ca
 */
public class CheatDialog extends JFrame implements ActionListener{
    FenetreTest mere;
    private JTextArea zoneTexte, zoneMessage;
    private JScrollPane defilement;
    private JComboBox target, source;
    private JLabel targetLb, sourceLb;
    private JButton execute, cancel;
    
    /** Creates a new instance of CheatDialog */
    public CheatDialog(FenetreTest m)
    {
        mere=m;
        Rectangle b = VGame.instance.getBounds();
        
        int w = 450;
        int h = 730;

        b.setLocation( (int)(b.getX() + b.getWidth()) - (w + 10),
                       (int)(b.getY() + (b.getHeight() - h) / 2) );
        b.setSize( w, h );
        
        setBounds(b);
        setTitle("Cheats");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contenant=getContentPane();
        contenant.setLayout(null);
     
        Rectangle emplacement=new Rectangle();
   
        targetLb=new JLabel("Target");
        emplacement.setRect(10,40,90,25);
        targetLb.setBounds(emplacement);
        contenant.add(targetLb);
        
        target = new JComboBox();
        emplacement.setRect(90,40,300,25);
        target.setBounds(emplacement);
        contenant.add(target);
        
        targetLb=new JLabel("Source");
        emplacement.setRect(10,70,90,25);
        targetLb.setBounds(emplacement);
        contenant.add(targetLb);
        
        source = new JComboBox();
        emplacement.setRect(90,70,300,25);
        source.setBounds(emplacement);
        contenant.add(source);
                
        zoneTexte=new JTextArea(200,200);
        zoneTexte.setLineWrap(true); 
        zoneTexte.setWrapStyleWord(true);
        zoneTexte.setText("");
       
        JPanel panneau=new JPanel();
        panneau.setBounds(10,100,400,250);
        
        defilement = new JScrollPane(zoneTexte,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        defilement.setPreferredSize(new Dimension(400,200));
        panneau.add(defilement);
        contenant.add(panneau);
        
        zoneMessage=new JTextArea("Enter scripts commands, one per line. Be very careful, you can easily screw the game with this.");
        zoneMessage.setLineWrap(true); 
        zoneMessage.setWrapStyleWord(true); 
        zoneMessage.setEditable(false);
        zoneMessage.setBounds(10,350,400,40);
        contenant.add(zoneMessage);
        
        execute=new JButton("Execute");
        emplacement.setRect(10,400,80,25);
        execute.setBounds(emplacement);
        execute.addActionListener(this);
        contenant.add(execute);
                
        cancel=new JButton("Cancel");
        emplacement.setRect(300,400,80,25);
        cancel.setBounds(emplacement);
        cancel.addActionListener(this);
        contenant.add(cancel);
        
        StatChar temp;
        for(int k=0;k<FenetreTest.instance.getTargetCount();k++){
            temp=FenetreTest.instance.getTarget( k );
            target.addItem(temp);
            source.addItem(temp);
        }
        
        setContentPane(contenant);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent ev){
		try
		{
			if( ev.getSource() == execute )
			{
				mere.executeScript( zoneTexte.getText(),
					((StatChar)source.getSelectedItem()).mere,
					((StatChar)target.getSelectedItem()).mere );
			}
		}
		catch( Exception e )
		{
			Util.error( e );
		}
		
        dispose();
    }
}
