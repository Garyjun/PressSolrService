package com.channelsoft.appframe.dao.query;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;

/**
 * <dl>
 * Hibernate的查询条件
 * <dt>SPMS</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2004</dd>
 * <dd>Company: 北京青牛软件技术有限责任公司</dd>
 * <dd>CreateDate: 2005-6-16</dd>
 * </dl>
 * 
 * @author 李炜
 */
public class QueryConditionItem {
	private String fieldName;

	private Operator operator;

	private Object value;

	public String getConditionDesc() {
		return fieldName + " " + operator + " ?";
	}

	public String getConditionDesc(String alias) {
		StringBuffer condition = new StringBuffer(50);
		condition.append(alias).append(".").append(fieldName).append(" ")
				.append(operator).append(" ?");
		return condition.toString();
	}

	public void build(Criteria rootCriteria,
			Map<Object, Object> appendedCriteria) throws HibernateException {
		if (operator != Operator.ISNULL && operator != Operator.ISNOTNULL) {
			if (value == null || value.toString().trim().length() == 0) {
				return;
			}
		}

		// criteria.add(operator.buildCriteria(criteria,this));
		operator.buildCriteria(rootCriteria, appendedCriteria, this);
		// return operator.buildCriteria(rootCriteria, appendedCriteria, this);
	}

	/**
	 * 
	 */
	public QueryConditionItem() {
		super();
	}

	public QueryConditionItem(String fieldName, Operator operator, Object value) {
		super();
		setFieldName(fieldName);
		setOperator(operator);
		setValue(value);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public Object getCalValue() {
		//modify by Fuwenbin 2006-12-15
//		if (getOperator() == Operator.LIKE) {
//			if (String.class.isInstance(getValue())) {
//				return getValue() + "%";
//			}
//		}
		
		if (String.class.isInstance(getValue())) {
			if (getOperator() == Operator.LIKE) {
				return getValue() + "%";
			}
			
			if (getOperator() == Operator.LIKEANYWHERE) {
				return "%" + getValue() + "%";
			}
		}
		//end
		
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String toString() {
		return getFieldName() + getOperator().getOerator() + getValue();
	}

	public boolean isEmptyValue() {
		return getValue() == null || getValue().toString().trim().length() == 0;
	}
}