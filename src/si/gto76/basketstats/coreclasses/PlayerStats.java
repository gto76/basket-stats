package si.gto76.basketstats.coreclasses;

import java.util.ArrayList;
import java.util.List;

public class PlayerStats {
	//FGM-A	3PM-A	OFF	DEF	TOT	AST	PF	ST	TO	BS	PTS
	////////////////////////////////////////
	private final Team team;
	private Shots shots = new Shots();
	private int plusMinus = 0,
				off = 0, 
				def = 0,
				ast = 0,
				pf = 0,
				st = 0,
				to = 0,
				bs = 0;	
	private final List<Action> actions = new ArrayList<Action>();
	////////////////////////////////////////
	
	public PlayerStats(Team team) {
		this.team = team;
		for (Stat stat :  Stat.inputValues()) {
			actions.add(new Action(stat, this));
		}
	}
	
	public PlayerStats(Team team, int fgm, int fga, int tpm, int tpa, 
			int plusMinus, int off, int def, int ast, int pf, int st, int to, int bs) {
		this(team);
		this.shots = new Shots(fgm, fga, tpm, tpa);
		this.plusMinus = plusMinus;
		this.off = off;
		this.def = def;
		this.ast = ast;
		this.pf = pf;
		this.st = st;
		this.to = to;
		this.bs = bs;
	}
	
	////////////////////////////////////////

	//
	// SETERS:
	//
	/*
	 * Return value is for plus minus handling which is executed in SwingFiller class.
	 */
	public Integer made2p() {
		shots.made2p++;
		return 2;
	}
	public Integer unMade2p() {
		shots.made2p--;
		return -2;
	}
	public Integer missed2p() {
		shots.missed2p++;
		return null;
	}
	public Integer unMissed2p() {
		shots.missed2p--;
		return null;
	}
	
	public Integer made3p() {
		shots.made3p++;
		return 3;
	}
	public Integer unmade3p() {
		shots.made3p--;
		return -3;
	}
	public Integer missed3p() {
		shots.missed3p++;
		return null;
	}
	public Integer unMissed3p() {
		shots.missed3p--;
		return null;
	}

	public Integer changePlusMinus(int points) {
		plusMinus += points;
		return null;
	}
	
	public Integer madeOff() {
		off++;
		return null;
	}
	public Integer unMadeOff() {
		off--;
		return null;
	}

	public Integer madeDef() {
		def++;
		return null;
	}
	public Integer unmadeDef() {
		def--;
		return null;
	}

	public Integer madeAst() {
		ast++;
		return null;
	}
	public Integer unmadeAst() {
		ast--;
		return null;
	}

	public Integer madePf() {
		pf++;
		return null;
	}
	public Integer unmadePf() {
		pf--;
		return null;
	}

	public Integer madeSt() {
		st++;
		return null;
	}
	public Integer unmadeSt() {
		st--;
		return null;
	}

	public Integer madeTo() {
		to++;
		return null;
	}
	public Integer unmadeTo() {
		to--;
		return null;
	}

	public Integer madeBs() {
		bs++;
		return null;
	}
	public Integer unmadeBs() {
		bs--;
		return null;
	}
	
	////////////////////////////////////////
	
	//
	// GETTERS
	//
	
	//FGM-A	3PM-A	OFF	DEF	TOT	AST	PF	ST	TO	BS	PTS
	public int get(Stat statCat) {
		switch (statCat) {
			case AST : return getAst();
			case BS: return getBs();
			case DEF: return getDef();
			case FGA: return getFga();
			case FGM: return getFgm();
			case PM: return getPlusMinus();
			case OFF: return getOff();
			case PF: return getPf();
			case PTS: return getPts();
			case REB: return getReb();
			case ST: return getSt();
			case TO: return getTo();
			case TPA: return getTpa();
			case TPM: return getTpm();
			case TPF: return getTpf();
			case IIPM: return get2pm();
			case IIPF: return get2pf();
			default : return -1;
		}
	}
	
	public int getFgm() {
		return shots.getFgm();
	}
	public int getFga() {
		return shots.getFga();
	}
	public int getTpm() {
		return shots.getTpm();
	}
	public int getTpa() {
		return shots.getTpa();
	}
	public int getTpf() {
		return shots.getTpf();
	}
	public int get2pm() {
		return shots.get2pm();
	}
	public int get2pf() {
		return shots.get2pf();
	}
	public int getPts() {
		return shots.getPts();
	}
	
	public int getPlusMinus() {
		return plusMinus;
	}

	public int getOff() {
		return off;
	}
	public int getDef() {
		return def;
	}
	public int getReb() {
		return off + def;
	}
	public int getAst() {
		return ast;
	}
	public int getPf() {
		return pf;
	}
	public int getSt() {
		return st;
	}
	public int getTo() {
		return to;
	}
	public int getBs() {
		return bs;
	}
	
	////////////////////////////////////////

	public List<Action> getActions() {
		return actions;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public Player getPlayer() {
		return team.getPlayer(this);
	}
	
	@Override
	public String toString() {
		//FGM-A	3PM-A +/-	OFF	DEF	TOT	AST	PF	ST	TO	BS	PTS
		StringBuilder sb = new StringBuilder();
		sb.append(Team.padTab(getFgm()+"-"+getFga()))
		.append(Team.padTab(getTpm()+"-"+getTpa()));
		for (Stat sc : Stat.nonScoringValues()) {
			sb.append(Team.padTab(Integer.toString(get(sc))));
		}
		return sb.toString();
	}

}