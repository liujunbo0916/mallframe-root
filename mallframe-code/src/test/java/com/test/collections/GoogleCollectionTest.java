package com.test.collections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * 本类中对google的collection 基本的api做了简单的演示。
 * @author Administrator
 *
 */
public class GoogleCollectionTest {
	
	/**
	 * Immutable Collections   不可变集合
	 */
	@Test 
	public  void immutableTest(){
		
		ImmutableSet<String> immutableSet = ImmutableSet.of("RED", "GREEN","GREEN"); 
		/**
		 * 改变原集合   immutableSet不会被改变  相当于深度复制    Immutable类集合效率高于jdkcollection
		 */ 
		Set<String> set = new HashSet<>(Arrays.asList(new String[]{"RED","GREEN","BLACK"}));
		ImmutableSet<String> immutableSet1 = ImmutableSet.copyOf(set);
		set.add("YELLOW");
		for(String s : immutableSet1){
			System.out.println(s);
		}
		/**
		 * 会抛出  异常  因为是一个不可变集合
		 */
		// immutableSet1.add("ORANGE");
		//另一种创建方式    
		Builder<String> builder =  ImmutableSet.builder(); 
		ImmutableSet<String> immutableSet2 = builder.add("RED").addAll(set).build(); 
		for(String s : immutableSet2){
			System.out.println(s);
		}
		
	}
	
	/**
	 * Multi 集合测试
	 * Multiset 并没有实现 java.util.Set 接口 而是实现了collect接口，它更像是一个 Bag。普通的 Set 就像这样 :[car, ship, bike]，
	 * 而 Multiset 会是这样 : [car x 2, ship x 6, bike x 3]   
	 */
	@Test
	public void multiTest(){
		HashMultiset<String> multiSet = HashMultiset.create(); 
		multiSet.add("RED");
		multiSet.add("RED");
		multiSet.add("GREEN");
		multiSet.add("GREEN");
		multiSet.add("GREEN");
		multiSet.add("BLACK");
		multiSet.add("BLACK");
		multiSet.add("BLACK");
		multiSet.add("BLACK");
		System.out.println(multiSet.count("RED"));
		//循环multiSet会将所有的元素循环出来 
		for(String s : multiSet){
			System.out.println(s);
		}
		/**
		 * 
		 * HashMultimap: key 放在 HashMap，而 value 放在 HashSet，即一个 key 对应的 value 不可重复
		 *	ArrayListMultimap: key 放在 HashMap，而 value 放在 ArrayList，即一个 key 对应的 value 有顺序可重复
		 *	LinkedHashMultimap: key 放在 LinkedHashMap，而 value 放在 LinkedHashSet，即一个 key 对应的 value 有顺序不可重复
		 *	TreeMultimap: key 放在 TreeMap，而 value 放在 TreeSet，即一个 key 对应的 value 有排列顺序
		 *	ImmutableMultimap: 不可修改的 Multimap
		 */
		HashMultimap<String, String> multimap = HashMultimap.create();
		multimap.put("1", "liujunbo");
		multimap.put("1", "liguangye");
		multimap.put("1", "zhangsan");
		multimap.put("2", "lisi");
		multimap.put("2", "wangerma");
		Set<String> valus = multimap.get("1");
		/**
		 * #########################hashMap######################################
		 */
		System.out.println("#########################hashMap######################################");
		  HashMap<String, Integer> hashMap=new HashMap<String,Integer>();  
	        hashMap.put("hadoop", 23);  
	        hashMap.put("hadoop", 22);  
	        hashMap.put("spark", 56);  
	        hashMap.put("spark", 58);  
	        hashMap.put("storm", 78);  
	        hashMap.put("", 78);  
	        hashMap.put(null, 78);  
	        hashMap.put(null, 79);  
	        for(String demo:hashMap.keySet()){  
	            System.out.println(demo+"\t"+hashMap.get(demo));  
	        }  
	        System.out.println("#########################HashMultimap######################################");  
	        HashMultimap<String,Integer> hashMultimap = HashMultimap.create();  
	        hashMultimap.put("hadoop", 23);  
	        hashMultimap.put("hadoop", 22);  
	        hashMultimap.put("spark", 56);  
	        hashMultimap.put("spark", 58);  
	        hashMultimap.put("storm", 78);  
	        hashMultimap.put("", 78);  
	        hashMultimap.put(null, 78);  
	        for(String demo:hashMultimap.keySet()){  
	        	Set<Integer> obj = hashMultimap.get(demo);
	            System.out.println(demo+"\t"+hashMultimap.get(demo));  
	        }  
	        for (Entry<String , Integer> entry: hashMultimap.entries()) {
				System.out.println(entry.getValue());
			}
	        
	}
}
