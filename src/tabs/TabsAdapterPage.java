package tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAdapterPage extends FragmentPagerAdapter
{
	public TabsAdapterPage(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int index)
	{
		switch (index)
		{
		case 0:
			// Upcomming
			return new Upcomming();
		case 1:
			// Scan QR
			return new QRscan();
		case 2:
			// Search
			return new Search();
		}
		return null;
	}

	@Override
	public int getCount()
	{
		// get item count - equal to number of tabs
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return "OBJECT " + (position + 1);
	}

}
