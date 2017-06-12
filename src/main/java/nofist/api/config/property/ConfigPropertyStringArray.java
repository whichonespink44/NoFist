package nofist.api.config.property;


public class ConfigPropertyStringArray extends ConfigProperty {

    public String[] valueStringArray;

    public ConfigPropertyStringArray(Type type, String name, String category, String description, String[] defaultValue) {

        super(type, name, category, description);

        this.valueStringArray = defaultValue;

        this.formatDescription();
    }

    public String[] get() {
        return this.valueStringArray;
    }

    public void set(String[] value) {
        this.valueStringArray = value;
    }
}
