package sjsu.chetwood.cs146.project3;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;


public class RBTTester {

	@Test
	public void test() {
		RedBlackTree<String> rbt = new RedBlackTree<>();
		rbt.insert("D");
		rbt.insert("B");
		rbt.insert("A");
		rbt.insert("C");
		rbt.insert("F");
		rbt.insert("E");
		rbt.insert("H");
		rbt.insert("G");
		rbt.insert("I");
		rbt.insert("J");
		assertEquals("DBACFEHGIJ", makeString(rbt));
		String str=     
				"Color: 1, Key:D Parent: \n"+
						"Color: 1, Key:B Parent: D\n"+
						"Color: 1, Key:A Parent: B\n"+
						"Color: 1, Key:C Parent: B\n"+
						"Color: 1, Key:F Parent: D\n"+
						"Color: 1, Key:E Parent: F\n"+
						"Color: 0, Key:H Parent: F\n"+
						"Color: 1, Key:G Parent: H\n"+
						"Color: 1, Key:I Parent: H\n"+
						"Color: 0, Key:J Parent: I\n";
		assertEquals(str, makeStringDetails(rbt));

	}

	@Test
	public void testDictionaryCreation() throws Exception {	
		RedBlackTree<String> rbt = new RedBlackTree<>();
		File file = new File("dictionary.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));

		String str;
		long start = System.currentTimeMillis();
		while((str = br.readLine()) != null) {
			rbt.insert(str);
		}
		long end = System.currentTimeMillis();
		long elapsed = end - start;
		System.out.println("Time to insert dictionary.txt into Red Black Tree: " + elapsed);
		br.close();
	}

	@Test
	public void  testSearch() throws Exception {

		ArrayList<String> dict = new ArrayList<>();

		RedBlackTree<String> rbt = new RedBlackTree<>();
		File file = new File("dictionary.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));


		String str;
		while((str = br.readLine()) != null) {
			rbt.insert(str);
			dict.add(str);
		}

		Random seed = new Random();
		int rand = seed.nextInt(349900);
		String searchForThis = dict.get(rand);
		System.out.println("Searching for the random word: " + searchForThis);

		long start = System.currentTimeMillis();
		RBNode<String> found = rbt.search(searchForThis);
		long end = System.currentTimeMillis();
		long ellapsed = end-start;
		System.out.println("Time to search for the random word " + searchForThis + " was " + ellapsed);
		System.out.println("Node has the word: " + found.getDatum());

		br.close();
	}

	@Test
	public void testMin() throws Exception {
		RedBlackTree<String> dict = getDict();
		// aaaa first entry in dictionary.txt
		String min = dict.minimum().getDatum();
		assertEquals("aaaa", min);
	}

	@Test
	public void testMax() throws Exception {
		RedBlackTree<String> dict = getDict();
		// zzzzzzzz last entry in dictionary.txt
		String max = dict.maximum().getDatum();
		assertEquals("zzzzzzzz", max);
	}

	@Test
	public void testSuccessor() {

	}

	public static RedBlackTree<String> getDict() throws Exception {
		RedBlackTree<String> rbt = new RedBlackTree<>();
		File file = new File("dictionary.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));


		String str;
		while((str = br.readLine()) != null) {
			rbt.insert(str);
		}
		br.close();
		return rbt;
	}

	public static String makeString(RedBlackTree<String> t)
	{

		class MyVisitor<E extends Comparable<E>> implements RedBlackTree.Visitor<E> {
			String result = "";
			public void visit(RBNode<E> n)
			{
				result = result + n.getDatum();
			}
		};

		MyVisitor<String> v = new MyVisitor<>();
		t.preOrderVisit(v);
		return v.result;
	}

	public static String makeStringDetails(RedBlackTree<String> t) {
		{
			class MyVisitor<E extends Comparable<E>> implements RedBlackTree.Visitor<E> {
				String result = "";
				public void visit(RBNode<E> n)
				{
					int color = n.isRed() ? 0 : 1;
					if(n.getParent() == null) {
						result += "Color: "
								+color+", "
								+ "Key:"+
								n.getDatum()+
								" Parent: "+
								//n.getParent().getDatum()+
								"\n";
					}
					else {
						result += "Color: "
								+color+", "
								+ "Key:"+
								n.getDatum()+
								" Parent: "+
								n.getParent().getDatum()+
								"\n";
					}
				}
			};

			MyVisitor<String> v = new MyVisitor<>();
			t.preOrderVisit(v);
			return v.result;
		}
	}
}

