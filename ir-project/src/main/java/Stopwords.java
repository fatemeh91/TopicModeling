/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fatemeh
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import weka.core.RevisionHandler;
import weka.core.RevisionUtils;

public class Stopwords implements RevisionHandler {
	public int Docno;
	protected HashSet m_Words = null;
	protected static Stopwords m_Stopwords;

	static {
		if (m_Stopwords == null) {
			m_Stopwords = new Stopwords();
		}
	}

	public void setDocno(int Docno) {
		this.Docno = Docno;
	}

	public int getDocno() {
		return Docno;
	}

	public Stopwords() {

		m_Words = new HashSet();

		// Stopwords list from Rainbow
		add(".");
		add("'");
		add(",");
		add("�");
		add("a");
		add("able");
		add("about");
		add("above");
		add("according");
		add("accordingly");
		add("across");
		add("actually");
		add("after");
		add("afterwards");
		add("again");
		add("against");
		add("all");
		add("allow");
		add("allows");
		add("almost");
		add("alone");
		add("along");
		add("already");
		add("also");
		add("although");
		add("always");
		add("am");
		add("among");
		add("amongst");
		add("an");
		add("and");
		add("another");
		add("any");
		add("anybody");
		add("anyhow");
		add("anyone");
		add("anything");
		add("anyway");
		add("anyways");
		add("anywhere");
		add("apart");
		add("appear");
		add("eof");
		add("appreciate");
		add("appropriate");
		add("are");
		add("around");
		add("as");
		add("aside");
		add("ask");
		add("asking");
		add("associated");
		add("at");
		add("available");
		add("away");
		add("awfully");
		add("b");
		add("be");
		add("became");
		add("because");
		add("become");
		add("becomes");
		add("becoming");
		add("been");
		add("before");
		add("beforehand");
		add("behind");
		add("being");
		add("believe");
		add("below");
		add("beside");
		add("besides");
		add("best");
		add("better");
		add("between");
		add("beyond");
		add("both");
		add("brief");
		add("but");
		add("by");
		add("c");
		add("came");
		add("can");
		add("cannot");
		add("cant");
		add("cause");
		add("causes");
		add("certain");
		add("certainly");
		add("changes");
		add("clearly");
		add("co");
		add("com");
		add("come");
		add("comes");
		add("concerning");
		add("consequently");
		add("consider");
		add("considering");
		add("contain");
		add("containing");
		add("contains");
		add("corresponding");
		add("could");
		add("course");
		add("currently");
		add("d");
		add("definitely");
		add("described");
		add("despite");
		add("did");
		add("different");
		add("do");
		add("does");
		add("doing");
		add("done");
		add("down");
		add("downwards");
		add("during");
		add("e");
		add("each");
		add("edu");
		add("eg");
		add("eight");
		add("either");
		add("else");
		add("elsewhere");
		add("enough");
		add("entirely");
		add("especially");
		add("et");
		add("etc");
		add("even");
		add("ever");
		add("every");
		add("everybody");
		add("everyone");
		add("everything");
		add("everywhere");
		add("ex");
		add("exactly");
		add("example");
		add("except");
		add("f");
		add("far");
		add("few");
		add("fifth");
		add("first");
		add("five");
		add("followed");
		add("following");
		add("follows");
		add("for");
		add("former");
		add("formerly");
		add("forth");
		add("four");
		add("from");
		add("further");
		add("furthermore");
		add("g");
		add("get");
		add("gets");
		add("getting");
		add("given");
		add("gives");
		add("go");
		add("goes");
		add("going");
		add("gone");
		add("got");
		add("gotten");
		add("greetings");
		add("h");
		add("had");
		add("happens");
		add("hardly");
		add("has");
		add("have");
		add("having");
		add("he");
		add("hello");
		add("help");
		add("hence");
		add("her");
		add("here");
		add("hereafter");
		add("hereby");
		add("herein");
		add("hereupon");
		add("hers");
		add("herself");
		add("hi");
		add("him");
		add("himself");
		add("his");
		add("hither");
		add("hopefully");
		add("how");
		add("howbeit");
		add("however");
		add("i");
		add("ie");
		add("if");
		add("ignored");
		add("immediate");
		add("in");
		add("inasmuch");
		add("inc");
		add("indeed");
		add("indicate");
		add("indicated");
		add("indicates");
		add("inner");
		add("insofar");
		add("instead");
		add("into");
		add("inward");
		add("is");
		add("it");
		add("its");
		add("itself");
		add("j");
		add("just");
		add("k");
		add("keep");
		add("keeps");
		add("kept");
		add("know");
		add("knows");
		add("known");
		add("l");
		add("last");
		add("lately");
		add("later");
		add("latter");
		add("latterly");
		add("least");
		add("less");
		add("lest");
		add("let");
		add("like");
		add("liked");
		add("likely");
		add("little");
		add("ll"); // added to avoid words like you'll,I'll etc.
		add("look");
		add("looking");
		add("looks");
		add("ltd");
		add("m");
		add("mainly");
		add("many");
		add("may");
		add("maybe");
		add("me");
		add("mean");
		add("meanwhile");
		add("merely");
		add("might");
		add("more");
		add("moreover");
		add("most");
		add("mostly");
		add("much");
		add("must");
		add("my");
		add("myself");
		add("n");
		add("name");
		add("namely");
		add("nd");
		add("near");
		add("nearly");
		add("necessary");
		add("need");
		add("needs");
		add("neither");
		add("never");
		add("nevertheless");
		add("new");
		add("next");
		add("nine");
		add("no");
		add("nobody");
		add("non");
		add("none");
		add("noone");
		add("nor");
		add("normally");
		add("not");
		add("nothing");
		add("novel");
		add("now");
		add("nowhere");
		add("o");
		add("obviously");
		add("of");
		add("off");
		add("often");
		add("oh");
		add("ok");
		add("okay");
		add("old");
		add("on");
		add("once");
		add("one");
		add("ones");
		add("only");
		add("onto");
		add("or");
		add("other");
		add("others");
		add("otherwise");
		add("ought");
		add("our");
		add("ours");
		add("ourselves");
		add("out");
		add("outside");
		add("over");
		add("overall");
		add("own");
		add("p");
		add("particular");
		add("particularly");
		add("per");
		add("perhaps");
		add("placed");
		add("please");
		add("plus");
		add("possible");
		add("presumably");
		add("probably");
		add("provides");
		add("q");
		add("que");
		add("quite");
		add("qv");
		add("r");
		add("rather");
		add("rd");
		add("re");
		add("really");
		add("reasonably");
		add("regarding");
		add("regardless");
		add("regards");
		add("relatively");
		add("respectively");
		add("right");
		add("s");
		add("said");
		add("same");
		add("saw");
		add("say");
		add("saying");
		add("says");
		add("second");
		add("secondly");
		add("see");
		add("seeing");
		add("seem");
		add("seemed");
		add("seeming");
		add("seems");
		add("seen");
		add("self");
		add("selves");
		add("sensible");
		add("sent");
		add("serious");
		add("seriously");
		add("seven");
		add("several");
		add("shall");
		add("she");
		add("should");
		add("since");
		add("six");
		add("so");
		add("some");
		add("somebody");
		add("somehow");
		add("someone");
		add("something");
		add("sometime");
		add("sometimes");
		add("somewhat");
		add("somewhere");
		add("soon");
		add("sorry");
		add("specified");
		add("specify");
		add("specifying");
		add("still");
		add("sub");
		add("such");
		add("sup");
		add("sure");
		add("t");
		add("take");
		add("taken");
		add("tell");
		add("tends");
		add("th");
		add("than");
		add("thank");
		add("thanks");
		add("thanx");
		add("that");
		add("thats");
		add("the");
		add("their");
		add("theirs");
		add("them");
		add("themselves");
		add("then");
		add("thence");
		add("there");
		add("thereafter");
		add("thereby");
		add("therefore");
		add("therein");
		add("theres");
		add("thereupon");
		add("these");
		add("they");
		add("think");
		add("third");
		add("this");
		add("thorough");
		add("thoroughly");
		add("those");
		add("though");
		add("three");
		add("through");
		add("throughout");
		add("thru");
		add("thus");
		add("to");
		add("together");
		add("too");
		add("took");
		add("toward");
		add("towards");
		add("tried");
		add("tries");
		add("truly");
		add("try");
		add("trying");
		add("twice");
		add("two");
		add("u");
		add("un");
		add("under");
		add("unfortunately");
		add("unless");
		add("unlikely");
		add("until");
		add("unto");
		add("up");
		add("upon");
		add("us");
		add("use");
		add("used");
		add("useful");
		add("uses");
		add("using");
		add("usually");
		add("uucp");
		add("v");
		add("value");
		add("various");
		add("ve"); // added to avoid words like I've,you've etc.
		add("very");
		add("via");
		add("viz");
		add("vs");
		add("w");
		add("want");
		add("wants");
		add("was");
		add("way");
		add("we");
		add("welcome");
		add("well");
		add("went");
		add("were");
		add("what");
		add("whatever");
		add("when");
		add("whence");
		add("whenever");
		add("where");
		add("whereafter");
		add("whereas");
		add("whereby");
		add("wherein");
		add("whereupon");
		add("wherever");
		add("whether");
		add("which");
		add("while");
		add("whither");
		add("who");
		add("whoever");
		add("whole");
		add("whom");
		add("whose");
		add("why");
		add("will");
		add("willing");
		add("wish");
		add("with");
		add("within");
		add("without");
		add("wonder");
		add("would");
		add("would");
		add("x");
		add("y");
		add("yes");
		add("yet");
		add("you");
		add("your");
		add("yours");
		add("yourself");
		add("yourselves");
		add("z");
		add("zero");
	}

	/**
	 * removes all stopwords
	 */
	public void clear() {
		m_Words.clear();
	}

	public void add(String word) {
		if (word.trim().length() > 0) {
			m_Words.add(word.trim().toLowerCase());
		}
	}

	public boolean remove(String word) {
		return m_Words.remove(word);
	}

	public boolean is(String word) {
		return m_Words.contains(word.toLowerCase());
	}

	public Enumeration elements() {
		Iterator iter;
		Vector list;

		iter = m_Words.iterator();
		list = new Vector();

		while (iter.hasNext()) {
			list.add(iter.next());
		}

		// sort list
		Collections.sort(list);
		return list.elements();
	}

	
	public ArrayList<STINT> preprocssingonfile(File file) throws Exception {
		
		ArrayList<STINT> WordDoc = read(new BufferedReader(new FileReader(file)));	
		return WordDoc;
	}

	public ArrayList<STINT> read(BufferedReader reader) throws Exception {
		boolean newdoc = false;
		ArrayList<STINT> WordDoc = new ArrayList<>();
		String line;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			line = NormalizingLine(line);
			
			if (line.contains("product") && line.contains("productId")){
				newdoc = true;
				Docno++;
				
			}
			
			
			
			if (line.contains("text") && line.contains("review")){
				line=line.replace("text", " ");
				line=line.replace("review", " ");
				
				newdoc = false;	
			}
			

			StringTokenizer tokens = new StringTokenizer(line);
			while (tokens.hasMoreTokens() &&  newdoc == false) {
				String word = tokens.nextToken();
				if (!is(word)) { // nt stop word
				//	Stemmer s = new Stemmer();
			//		for (int i = 0; i < word.length(); i++) {
				//		s.add(word.charAt(i));
				//	}
				//	s.stem();
				//	{
						STINT StWord=new STINT();
						
						StWord.word = word;
						StWord.docnumber=Docno;
						WordDoc.add(StWord);
					//}
				}
			}

		}
		
		reader.close();
		return WordDoc;
	}



	public String toString() {
		Enumeration enm;
		StringBuffer result;

		result = new StringBuffer();
		enm = elements();
		while (enm.hasMoreElements()) {
			result.append(enm.nextElement().toString());
			if (enm.hasMoreElements()) {
				result.append(",");
			}
		}

		return result.toString();
	}

	/**
	 * Returns true if the given string is a stop word.
	 *
	 * @param str
	 *            the word to test
	 * @return true if the word is a stopword
	 */
	public static boolean isStopword(String str) {
		return m_Stopwords.is(str.toLowerCase());
	}

	public String getRevision() {
		return RevisionUtils.extract("$Revision: 8034 $");
	}

	private String NormalizingLine(String line) {
		line=line.replaceAll("\\p{P}\\p{S}", " ");
		
		line = line.replace(",", " ");
		line = line.replace(".", " ");
		line = line.replace("'", " ");
		line = line.replace("�", " ");
		line = line.replace("#", " ");
		line = line.replace("/", " ");
		line = line.replace("\\", " ");
		line = line.replace("\\t", " ");
		line = line.replace("\\n", " ");
		line = line.replace("\\(", " ");
		line = line.replace("\\)", " ");
		line = line.replace(":", " ");
		line = line.replace("!", " ");
		line = line.replace("(", " ");
		line = line.replace(")", " ");
		line = line.replace("}", " ");
		line = line.replace("-", " ");
		line = line.replace("{", " ");
		line = line.replace("]", " ");
		line = line.replace("[", " ");
		line = line.replace("0", " ");
		line = line.replace("1", " ");
		line = line.replace("2", " ");
		line = line.replace("3", " ");
		line = line.replace("4", " ");
		line = line.replace("5", " ");
		line = line.replace("6", " ");
		line = line.replace("7", " ");
		line = line.replace("8", " ");
		line = line.replace("9", " ");
		line = line.replace("*", " ");

		return line;
	}


}