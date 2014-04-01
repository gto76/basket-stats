package si.gto76.basketstats.coreclasses;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Stat {
	private PlayerStats ps;
	private String name;
	private Team team;
	private StatCats statEnumValue;

	private String actionMethodName;
	private Method actionMethod;
	private String undoMethodName;
	private Method undoMethod;

	public Stat(PlayerStats ps, String methodName, String undoMethodName,
			String name, StatCats statEnumValue, Team team) {
		this.name = name;
		this.ps = ps;
		this.actionMethodName = methodName;
		this.undoMethodName = undoMethodName;
		this.statEnumValue = statEnumValue;
		this.team = team;
		createMethods();
	}

	private void createMethods() {
		try {
			Class<PlayerStats> c = PlayerStats.class;
			actionMethod = c.getDeclaredMethod(actionMethodName);
			undoMethod = c.getDeclaredMethod(undoMethodName);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public int getStat() {
		return ps.get(statEnumValue);
	}

	public String getName() {
		return name;
	}

	public Team getTeam() {
		return team;
	}

	public Player getPlayer() {
		return team.getPlayer(ps);
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
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

}