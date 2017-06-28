package com.example.type2.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by sjy on 2017/6/28.
 */

public class Header implements Iterable<MinimalField> {
    private final List<MinimalField> fields = new LinkedList();
    private final Map<String, List<MinimalField>> fieldMap = new HashMap();

    public Header() {
    }

    public void addField(MinimalField field) {
        if (field != null) {
            String key = field.getName().toLowerCase(Locale.US);
            List<MinimalField> values = this.fieldMap.get(key);
            if (values == null) {
                values = new LinkedList();
                this.fieldMap.put(key, values);
            }

            ((List) values).add(field);
            this.fields.add(field);
        }
    }

    public List<MinimalField> getFields() {
        return new ArrayList(this.fields);
    }

    public MinimalField getField(String name) {
        if (name == null) {
            return null;
        } else {
            String key = name.toLowerCase(Locale.US);
            List list = (List) this.fieldMap.get(key);
            return list != null && !list.isEmpty() ? (MinimalField) list.get(0) : null;
        }
    }

    public List<MinimalField> getFields(String name) {
        if (name == null) {
            return null;
        } else {
            String key = name.toLowerCase(Locale.US);
            List list = (List) this.fieldMap.get(key);
            return (List) (list != null && !list.isEmpty() ? new ArrayList(list) : Collections.emptyList());
        }
    }

    public int removeFields(String name) {
        if (name == null) {
            return 0;
        } else {
            String key = name.toLowerCase(Locale.US);
            List removed = (List) this.fieldMap.remove(key);
            if (removed != null && !removed.isEmpty()) {
                this.fields.removeAll(removed);
                return removed.size();
            } else {
                return 0;
            }
        }
    }

    public void setField(MinimalField field) {
        if (field != null) {
            String key = field.getName().toLowerCase(Locale.US);
            List list = (List) this.fieldMap.get(key);
            if (list != null && !list.isEmpty()) {
                list.clear();
                list.add(field);
                int firstOccurrence = -1;
                int index = 0;

                for (Iterator it = this.fields.iterator(); it.hasNext(); ++index) {
                    MinimalField f = (MinimalField) it.next();
                    if (f.getName().equalsIgnoreCase(field.getName())) {
                        it.remove();
                        if (firstOccurrence == -1) {
                            firstOccurrence = index;
                        }
                    }
                }

                this.fields.add(firstOccurrence, field);
            } else {
                this.addField(field);
            }
        }
    }

    public Iterator<MinimalField> iterator() {
        return Collections.unmodifiableList(this.fields).iterator();
    }

    public String toString() {
        return this.fields.toString();
    }
}
