package com.driver;


import java.util.*;

import org.springframework.stereotype.Repository;

import net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer.ForReturnTypeName;
//import net.bytebuddy.implementation.InvokeDynamic.InvocationProvider.NameProvider.ForExplicitName;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
    	orderMap.put(order.getId(), order);
        // your code here
    }

    public void savePartner(String partnerId){
    	partnerMap.put(partnerId, new DeliveryPartner(partnerId));
        // your code here
        // create a new partner with given partnerId and save it
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
        	orderToPartnerMap.put(orderId, partnerId);
        	DeliveryPartner partner = partnerMap.get(partnerId);
        	
        	partner.setNumberOfOrders(partner.getNumberOfOrders()+1);
        	if(!partnerToOrderMap.containsKey(partnerId)) {
        		partnerToOrderMap.put(partnerId, new HashSet<>());
        	}
        	
        	partnerToOrderMap.get(partnerId).add(orderId);
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
        }
    }

    public Order findOrderById(String orderId){
    	return orderMap.get(orderId);
        // your code here
    }

    public DeliveryPartner findPartnerById(String partnerId){
    	return partnerMap.get(partnerId);
        // your code here
    }

    public Integer findOrderCountByPartnerId(String partnerId){
    	if(partnerToOrderMap.containsKey(partnerId)) {
    		return partnerToOrderMap.get(partnerId).size();
    	}
    	return 0;
        // your code here
    }

    public List<String> findOrdersByPartnerId(String partnerId){
    	return new LinkedList<>(partnerToOrderMap.get(partnerId));
        // your code here
    }

    public List<String> findAllOrders(){
    	List<String> list = new LinkedList<>();
    	for (Map.Entry<String, Order> entry : orderMap.entrySet()) {
            list.add(entry.getKey());
        }
    	return list;

        // your code here
        // return list of all orders
    }

    public void deletePartner(String partnerId){
    	if(partnerMap.containsKey(partnerId))
    		partnerMap.remove(partnerId);
    	
    	for(String order:partnerToOrderMap.get(partnerId)) {
    		
    		orderToPartnerMap.remove(order);
    	}
    	partnerToOrderMap.remove(partnerId);
        // your code here
        // delete partner by ID
    }

    public void deleteOrder(String orderId){
    	if(orderMap.containsKey(orderId)) {
    		orderMap.remove(orderId);
    	}
    	DeliveryPartner partner = partnerMap.get(orderToPartnerMap.get(orderId));
    	partner.setNumberOfOrders(partner.getNumberOfOrders()-1);
    	partnerToOrderMap.get(orderToPartnerMap.get(orderId)).remove(orderId);
    	orderToPartnerMap.remove(orderId);
        // your code here
        // delete order by ID
    }

    public Integer findCountOfUnassignedOrders(){
    	int count = 0;
    	for(String order:orderMap.keySet()) {
    		if(!orderToPartnerMap.containsKey(order))
    			count++;
    	}
    	
    	return count;
        // your code here
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
    	int count = 0;
    	String []temp = timeString.split(":");
    	int time = Integer.parseInt(temp[0])*60+Integer.parseInt(temp[1]);
    	for(String order:partnerToOrderMap.get(partnerId)) {
    		if(orderMap.get(order).getDeliveryTime()>time)
    			count++;
    	}
    	
    	return count;
    	
        // your code here
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
    	
    	int lastTime = 0;
    	for(String order:partnerToOrderMap.get(partnerId)) {
    		
    		lastTime = Math.max(orderMap.get(order).getDeliveryTime(), lastTime);
    		
    	}
    	
    	int minutes = lastTime%60;
    	int hours = lastTime/60;
    	
    	String m=minutes+"",h=hours+"";
    	if(m.length()==1)
    		m = "0"+m;
    	
    	if(h.length()==1)
    		h = "0"+h;

    	return h+":"+m;
        // your code here
        // code should return string in format HH:MM
    }
}