import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.sql.*;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;


@SuppressWarnings("serial")
public class atmcard extends JFrame implements ActionListener
 {
    JFrame jf;
    Font f,f1;
	JButton b1,b2,b3;
	JLabel l1,l2,l3,l4;
	ImageIcon img1;
    JTextField t1;
   	JPasswordField pwd;
   	Connection con;
	PreparedStatement ps;
	Statement stmt;
	ResultSet rs;
    Timer t;
	int atno,acno,pno;
	String curdate;
    Date date;
    GregorianCalendar calendar;


    public atmcard ()
     {
     	date= new Date();
     	calendar=new GregorianCalendar();
	    calendar.setTime(date);
	    curdate =calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
	    System.out.println(curdate);

     	jf=new JFrame();
		f = new Font("Times New Roman",Font.BOLD,20);//button
		f1 = new Font("Times New Roman",Font.BOLD,25);//label
		jf.setLayout(null);
		jf.getContentPane().setBackground( Color.yellow);


		/*try{sound = Applet.newAudioClip(wavFile.toURL());}
        catch(Exception e){e.printStackTrace();}*/

		l4=new JLabel("ATM Card Number");
	    l4.setFont(new Font("Times New ROman",Font.BOLD,30));
	    l4.setForeground(Color.BLUE);
		l4.setBounds(250,250,300,30);
		jf.add(l4);

    	l2=new JLabel("Enter ATM card no:");
		l2.setFont(f1);
		l2.setForeground(Color.black);
		l2.setBounds(120,380,250,30);
		jf.add(l2);

		t1 = new JTextField(20);
		t1.setBounds(370,380,200,30);
		jf.add(t1);

		l3=new JLabel("Enter PIN no:");
		l3.setFont(f1);
		l3.setForeground(Color.black);
		l3.setBounds(120,430,250,30);
		jf.add(l3);

		pwd=new JPasswordField(10);
		pwd.setFont(f1);
		pwd.setBounds(370,430,200,30);
		jf.add(pwd);

		b1=new JButton("Enter",new ImageIcon("ok.png"));
		b1.setFont(f);
		b1.setBounds(120,550,130,40);jf.add(b1);
		b1.addActionListener(this);

		b2=new JButton("Clear",new ImageIcon("clear.png"));
		b2.setFont(f);
		b2.setBounds(280,550,130,40);jf.add(b2);
		b2.addActionListener(this);

		b3=new JButton("Cancel",new ImageIcon("cancel.png"));
		b3.setFont(f);
		b3.setBounds(440,550,130,40);jf.add(b3);
		b3.addActionListener(this);

        img1=new ImageIcon("bank.jpg");
		l1=new JLabel(img1);
		l1.setBounds(1,1,800,700);
        jf.add(l1);

         jf.setTitle("ATM CARD NO");
	     jf.setSize(800,700);
		 jf.setLocation(220,20);
		 jf.setResizable(false);
	     jf.setVisible(true);

	     t =new Timer(20000,this);// 20 minisecond
         t.start();

    }

     @SuppressWarnings({"unlikely-arg-type" })
	public void actionPerformed(ActionEvent ae)
     {
     	if(ae.getSource()==t)
     	{
     			t.stop();
 int reply=JOptionPane.showConfirmDialog(null,"Do you want continue?","ATM Time Warning",JOptionPane.YES_NO_OPTION);

	             if (reply == JOptionPane.YES_OPTION)
	   			{
	   			 
	   				t.start();
	   		    }
	   		  else if (reply == JOptionPane.NO_OPTION)
	   		    {
	   		     
	   		    	t.stop();
                   new Welcome();
         	       jf.setVisible(false);
		        }
     	}
		 else if(ae.getSource()==b1)
		{
		 
           	t.stop();
	    try
	     {
	   		if(((t1.getText()).equals(""))&&((pwd.getPassword()).equals("")))
	        {
		    JOptionPane.showMessageDialog(this,"Please enter ATM card no and PIN no!","Warning",JOptionPane.WARNING_MESSAGE);
		   
	        }
	        else
	        {
	        int foundrec = 0;
	        Class.forName("com.mysql.jdbc.Driver");
		    con=DriverManager.getConnection("jdbc:mysql://localhost:3308/test?useSSL=false","root","Piyush#1234");
		    System.out.println("Connected to database.");
            StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("select * from accountdetail where atmno=");
			stringBuilder.append(t1.getText());
			stringBuilder.append(" and pinno=");
			stringBuilder.append(pwd.getPassword());
			stringBuilder.append(" ");
			ps=con.prepareStatement(stringBuilder.toString());
	        rs=ps.executeQuery();
		    while(rs.next())
	        {

	        atno=rs.getInt(1);   System.out.println(atno);
	        acno=rs.getInt(2);   System.out.println(acno);
	        pno=rs.getInt(3);     System.out.println(pno);
	        String cardname=rs.getString(5);  System.out.println(cardname);
	        String expdate=rs.getString(7);

	        try
        	{

    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		Date date1 = sdf.parse(expdate);
        	Date date2 = sdf.parse(curdate);

        	System.out.println("expiry date of atm card:"+sdf.format(date1));
        	System.out.println("Today date:"+sdf.format(date2));

        	if(date1.compareTo(date2)>=0)
        	{
        		System.out.println("Expiry date of atm:"+sdf.format(date1)+"is after today date:"+sdf.format(date2));
        		 JOptionPane.showMessageDialog(null,"Hello "+cardname);
	             
	             new AccountType(atno,acno,pno);
	             jf.setVisible(false);
        	}
        	else if(date1.compareTo(date2)<0)
        	{
        		System.out.println("Date1 is before Date2");
        JOptionPane.showMessageDialog(this,"Your atm card is out of expiry date.Please take new ATM card from your home bank.","Warning",JOptionPane.WARNING_MESSAGE);
		   
	             new Welcome();
	             jf.setVisible(false);

        	}
        	}
        	catch(ParseException ex)
        	{
        	System.out.println("Exception in date format"+ex);
    		ex.printStackTrace();
    	    }

	        foundrec = 1;

	       }
	       if (foundrec == 0)
             {
              JOptionPane.showMessageDialog(null,"Invalid ATM card no or PIN no.","Warning",JOptionPane.WARNING_MESSAGE);
              
              t1.setText("");
              pwd.setText("");
             }
  	      } con.close();
        }
        catch(SQLException se)
		{
		System.out.println(se);
	     
        }
	    catch(Exception e)
	     {
	     System.out.println(e);
		 
	     }
       }
		else if(ae.getSource()==b2)
		{
		 
			t1.setText("");
			pwd.setText("");

		}
		else if(ae.getSource()==b3)
		{
		 
           	JOptionPane.showMessageDialog(this,"Your last transaction cancel.");
	        
           	new Welcome();
           	jf.setVisible(false);
		}
     }
      /*  public static void main(String args[])
	  {
		new atmcard();
	 }  */
}
 