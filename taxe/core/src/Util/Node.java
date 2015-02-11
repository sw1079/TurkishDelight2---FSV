package Util;

import java.util.ArrayList;

public class Node<T> {
//Class for constructing trees and graphs using a node data structure
private T data;
private Node<T> parent;
private float nodeCost = 0;
private ArrayList<Node<T>> children = new ArrayList<Node<T>>();

public T getData()
{
	return data;
}

public void setData(T newData)
{
	data = newData;
}

public float getNodeCost()
{
	return nodeCost;
}

public void setNodeCost(float newNodeCost)
{
	nodeCost = newNodeCost;
}

public Node<T> getParent()
{
	return parent;
}

public boolean setParent(Node<T> newParent)
{
	//A node can only have one parent
	if(parent  != null)
	{
		if(parent.hasChild(this))
		{
			parent.removeChild(this);
		}
		else
		{
			return false;
		}
	}
	parent = newParent;
	if(!parent.hasChild(this))
	{
		parent.addChild(this);
	}
	return true;
}

public boolean hasParent()
{
	if(parent == null)
	{
		return false;
	}
	else
	{
		return true;
	}
}

public boolean addChild(Node<T> child)
{
	if(!hasChild(child))
	{
		children.add(child);
		return true;
	}
	else
	{
		return false;
	}
}

public boolean removeChild(Node<T> child)
{
	if(hasChild(child))
	{
		children.remove(child);
		return true;
	}
	else
	{
		return false;
	}
}

public ArrayList<Node<T>> getChildren()
{
	return children;
}

public boolean hasChild(Node<T> child)
{
	if(children.contains(child))
	{
		return true;
	}
	else
	{
		return false;
	}
}



}
