package com.softwareTest.timeline.Utility;

import java.io.Serializable;

public class JsonVisibilityLevel implements Serializable
{
	public interface AbstractView
	{
	}

	public interface BasicView extends AbstractView
	{
	}

	public interface NormalView extends BasicView
	{
	}

	public interface DetailedView extends NormalView
	{
	}
}
