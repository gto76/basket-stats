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

	public Stat(PlayerStats ps, String methodName, String undoMethodName,
			String name, Team team) {
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
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
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