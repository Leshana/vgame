///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Food.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.awt.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class FoodData implements Serializable
{
	public int FoodID; // Key field (must come first)
	
	public String Nom;
	public String Description;
	
	public String FoodIcon;

	public int Nutrition;
	public int Price;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class Food extends ObjInventaire
{
	public int nutrition;

	///////////////////////////////////////////////////////////////////////////////////////////////
	public Food(int foodID, PlayerStat m)
	{
		super(m, foodID);
		type=Util.ITEM_FOOD;
		
		loadFromData( DataUtil.getFoodData( foodID ) );
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public String getFoodString()
	{
		return Math.abs(nutrition) + " food";
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void loadFromData( FoodData foodData )
	{
		setName( foodData.Nom );
		setImage( foodData.FoodIcon );

		nutrition = foodData.Nutrition;
		price = foodData.Price;
		description = foodData.Description;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ObjInventaire
	public void initializeView( JPanel vitals, JPanel details )
	{
		super.initializeView( vitals, details );

		JLabel foodLbl = new JLabel();
		foodLbl.setText( getFoodString() );
		foodLbl.setIcon( Util.getIcon("item_food") );
		foodLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
		vitals.add( foodLbl );
	}

	@Override // ObjInventaire
	public void use()
	{
		super.use();
		mere.nourriture+=nutrition;
		dispose();
	}

	@Override // ObjInventaire
	public String getUseString()
	{
		return "Eat";
	}

	@Override // ObjInventaire
	public boolean canStack( ObjInventaire ob )
	{
		return ob.type == type && ob.objetID == objetID;
	}
}
