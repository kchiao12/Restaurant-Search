////import static org.junit.Assert.*;
//import org.junit.Test;
//
//import restaurants.LArests;
//
//
//public class LArestsTest {
//
//	@Test
//	public void testPage1() {
//		LArests larests = new LArests();
//		larests.read();
//		System.out.println("Enter street address:");
//		String str = "1463 Nogales St, Rowland Heights, CA 91748";
//		try {
//			larests.compareDist(larests.requestGoogle(str));
//			larests.printFormat();
//			larests.pageNext(1);
//			larests.pageBack(1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testPage2() {
//		LArests larests = new LArests();
//		larests.read();
//		System.out.println("Enter street address:");
//		String str = "1463 Nogales St, Rowland Heights, CA 91748";
//		try {
//			larests.compareDist(larests.requestGoogle(str));
//			larests.printFormat();
//			larests.pageNext(5);
//			larests.pageBack(5);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testPage3() {
//		LArests larests = new LArests();
//		larests.read();
//		System.out.println("Enter street address:");
//		String str = "1463 Nogales St, Rowland Heights, CA 91748";
//		try {
//			larests.compareDist(larests.requestGoogle(str));
//			larests.printFormat();
//			larests.pageNext(-1);
//			larests.pageBack(-1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}
