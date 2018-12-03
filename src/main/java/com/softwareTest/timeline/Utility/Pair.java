package com.softwareTest.timeline.Utility;

public class Pair<K extends Object,V extends Object>
{
	private K key;
	private V value;

	Pair(K k,V v)
	{
		key=k;
		value=v;
	}

	public K getKey()
	{
		return key;
	}

	public void setKey(K key)
	{
		this.key=key;
	}

	public V getValue()
	{
		return value;
	}

	public void setValue(V value)
	{
		this.value=value;
	}
}
