package si.gto76.basketstats.coreclasses;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Stat {
	private PlayerStats ps; 
	private String name;
	private Team team;
	
	private String actionMethodName;
	private Method actionMethod;
	private String undoMethodName;
	private Method undoMethod;
	
	public Stat(PlayerStats ps, String methodName, String undoMethodName, String name, Team team) {
		this.name = name;
		this.ps = ps;
		this.actionMethodName = methodName;
		this.undoMethodName = undoMethodName;
		this.team = team;
		createMetods();
	}
	
	private void createMetods() {
		try {
			Class<PlayerStats> c = PlayerStats.class;
			actionMethod = c.getDeclaredMethod(actionMethodName);
			undoMethod = c.getDeclaredMethod(undoMethodName);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}
	public Team getTeam() {
		return team;
	}
	
	public Integer fireAction() {
		return invokeMethod(actionMethod);
	}
	public Integer undoAction() {
		return invokeMethod(undoMethod);
	}
	
	private Integer invokeMethod(Method method) {
		try {
			return (Integer) method.invoke(ps);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}