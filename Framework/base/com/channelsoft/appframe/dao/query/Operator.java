package com.channelsoft.appframe.dao.query;

import java.util.Collection;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;



/**
 * <dl>
 * <dt>SPMS</dt>
 * <dd>Description:查询条件的操作符</dd>
 * <dd>Copyright: Copyright (C) 2004</dd>
 * <dd>Company: 北京青牛软件技术有限责任公司</dd>
 * <dd>CreateDate: 2005-6-16</dd>
 * </dl>
 * 
 * @author 李炜
 */
public abstract class Operator {
	public final static Operator IN = new In();
	public final static Operator INBYCOL = new INByCOL();
	public final static Operator INBYARY = new INByAry();
    public final static Operator ISNOTNULL = new IsNotNull();
    public final static Operator ISNULL = new IsNull();
    public final static Operator EQUAL = new Equal();

    public final static Operator LIKE = new Like();
    public final static Operator LIKEANYWHERE = new LikeAnyWhere();
    public final static Operator NOTEQUAL = new NotEqual();    
    
    public final static Operator LE = new Le();
    public final static Operator LT = new Lt();

        public final static Operator GE = new Ge();

        public final static Operator GT = new Gt();
    public Operator() {

    }

    public abstract String getOerator();

    public void buildCriteria(Criteria rootCriteria, Map<Object, Object> appendedCriteria,
            QueryConditionItem queryConditionItem) throws HibernateException {
		// for debug
//    	System.out.println("\n Start of buildCriteria(): ");
//    	System.out.println("\n queryConditionItem.getFieldName(): "+queryConditionItem.getFieldName());
//    	System.out.println("rootCriteria.associatedClass: "+rootCriteria.getCriteriaClass().getName());
        
        // 保存 本次调用应该使用的Criteria引用,缺省指向rootCriteria
        Criteria thisCriteria = rootCriteria;
        
        
        String pos[] = queryConditionItem.getFieldName().split("\\.");
        // getFieldName() could return strings like  "className.fieldName"
        // it is also possible "comp_id.fieldName"
        // we try to support the two modes
        String actualField = "";
        
            for (int i=0; i < pos.length; i++) {
            	
            	if(i == pos.length-1) {
            		// come to the last sector
            		actualField = pos[i]; 
            		break;
            	}
            	
            	if(pos[i].equals("comp_id")) {
            		actualField+=pos[i];
            		// comp_id appear in the getFieldName()
            		// assemble all the strings after this and treat it as simple attribute
            		for(int j = i+1; j<pos.length; j++) {
            			actualField += "."+pos[j];	
            		}
            		break;
            	}
            	


				// 在map中查找, 判断是否该子类已经被关联上
            	boolean associated = appendedCriteria.containsKey(pos[i]);
            	if(associated) {
            		Criteria associatedCriteria = (Criteria)appendedCriteria.get(pos[i]);
//					System.out.println("class: [" + associatedCriteria.getCriteriaClass(pos[i]).getName()
//							+ "] was associated with alias: [" + pos[i]
//							+ "] already, skip..");
					// 使用已经存在的criteria 
					thisCriteria =  associatedCriteria;
					
					
				} else {
					// 否则，从thisCriteria创建新的appendedCriteria
//					System.out.println("associating alias: [" + pos[i]
//							+ "] with thisCriteria..");
					
					thisCriteria = thisCriteria.createCriteria(pos[i],
							pos[i]);
					
					// 放到Map中
					appendedCriteria.put(pos[i], thisCriteria);
				}
	        	// for debug
//				Class cls = thisCriteria.getCriteriaClass();
//				System.out.println("pos["+i+"]="+pos[i]+",  thisCriteria.associatedClass: "
//						+ cls.getName());    				
            }            
            
//        System.out.println("actualField: "+actualField);
                
        // 如果不是级联搜索，cascadeCriteria即为传进来的rootCriteria, 见QueryConditionList.build()
        // 否则，cascadeCriteria是根据级联表名创建的criteria
        // 并且，如果该级联表的criteria已经被创建过了，不会重复创建
        // 关于这里的逻辑，参见CfgServiceDao.query()
        
        thisCriteria.add(buidExpression(actualField, queryConditionItem));
        
        
//        System.out.println("end of buildCriteria(): \n");
        return;
    }

    public abstract Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem);
}

/**
 * in查询
 * add by 李巍璐 on 2006.11.28 
 */
class In extends Operator {

    public String getOerator() {
        return "in";
    }

    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
    	String[] arr = queryConditionItem.getValue().toString().split(",");
        return Expression.in(fieldName, arr);
    }
}

/**
 * <dd>Description:参数类型为collection</dd>
 * <dd>CreateDate: Dec 4, 2009</dd>
 * @author Fuwenbin
 */
class INByCOL extends Operator {
    public String getOerator() {
        return "in";
    }

    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
        return Expression.in(fieldName, (Collection) queryConditionItem.getValue());
    }
}
 
 /**
  * <dd>Description:参数类型为object[]</dd>
  * <dd>CreateDate: Dec 4, 2009</dd>
  * @author Fuwenbin
  */
 class INByAry extends Operator {
     public String getOerator() {
         return "in";
     }

     public Criterion buidExpression(String fieldName,
             QueryConditionItem queryConditionItem) {
         return Expression.in(fieldName, (Object[]) queryConditionItem.getValue());
     }
 }

class NotEqual extends Operator {

    public String getOerator() {
        return "<>";
    }

    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
        return Expression.not(Expression.eq(fieldName, queryConditionItem.getValue()));
    }
    
}

/**
 * 2005-07-14 BY LIWEI
 * 改为前匹配
 */
class Like extends Operator {

    public String getOerator() {
        return "like";
    }

    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
        return Expression.like(fieldName, queryConditionItem.getValue()
                .toString(), MatchMode.START);
    }
}

/**
 * 2005-08-01 BY liyong
 * 匹配所有
 */
class LikeAnyWhere extends Operator {

    public String getOerator() {
        return "like";
    }

    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
        return Expression.like(fieldName, queryConditionItem.getValue()
                .toString(), MatchMode.ANYWHERE);
    }
}


class Le extends Operator {

    public String getOerator() {
        return "<=";
    }

    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
        return Expression.le(fieldName, queryConditionItem.getValue());
    }
}

class Lt extends Operator {

    public String getOerator() {
        return "<";
    }

    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
        return Expression.lt(fieldName, queryConditionItem.getValue());
    }
}
class Gt extends Operator {

    public String getOerator() {
        return ">";
    }

    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
        return Expression.gt(fieldName, queryConditionItem.getValue());
    }
}
class Ge extends Operator {

    public String getOerator() {
        return ">=";
    }

    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
        return Expression.ge(fieldName, queryConditionItem.getValue());
    }
}

class Equal extends Operator {

    public String getOerator() {
        return "=";
    }
    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
    	
        return Expression.eq(fieldName, queryConditionItem.getValue());
    }
}

class IsNull extends Operator {
    public String getOerator() {
        return "Is Null";
    }
    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
        return Expression.isNull(fieldName);
    }
}

class IsNotNull extends Operator {
    public String getOerator() {
        return "Is Not Null";
    }
    public Criterion buidExpression(String fieldName,
            QueryConditionItem queryConditionItem) {
        return Expression.isNotNull(fieldName);
    }
}
