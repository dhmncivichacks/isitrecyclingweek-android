package theputnams.net.isitrecyclingweek.util;

public class INavItem implements NavItem
{
    protected String text;

    public INavItem(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
