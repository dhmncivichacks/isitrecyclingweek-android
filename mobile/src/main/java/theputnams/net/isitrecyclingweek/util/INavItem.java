package theputnams.net.isitrecyclingweek.util;

public class INavItem implements NavItem
{
    protected NavLocation location;

    public INavItem(NavLocation location) {
        this.location = location;
    }

    @Override
    public NavLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(NavLocation location) {
        this.location = location;
    }
}
