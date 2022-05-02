package pro.alxerxc.menuMaker.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Getter
public class Pagination {
    private static final int DEFAULT_MAX_PAGINATION_LINKS = 5;

    private final Page page;
    private final Params params;
    private List<LinkInfo> pageLinks;
    private final int maxPaginationLinks;

    public static Pagination of(Page page, Params params, int maxPaginationLinks) {
        return new Pagination(page, params, maxPaginationLinks);
    }
    public static Pagination of(Page page, Params params) {
        return of(page, params, DEFAULT_MAX_PAGINATION_LINKS);
    }

    /** Creates Sort by given string array of property names and directions.
     *
     * @param sortClauses can be: {"name,asc"} or {"id,desc","name,asc"} or {"name", "asc"} (by pairs)
     * @return tuned Sort object
     */
    public static Sort sort(String[] sortClauses) {
        if (sortClauses.length == 0) {
            return Sort.unsorted();
        }

        // Flatten clauses array here - collect all order elements in one plain list
        // Because strings in array could be paired in one element like {"field1,asc", "field2,desc"} here,
        // or placed in adjacent elements: {"field", "asc"}
        List<String> orderInstructions = new ArrayList<>();
        for (String sortClause: sortClauses) {
            String[] parts = sortClause.split(",");
            if (parts.length >= 1) {
                orderInstructions.add(parts[0]);
            }
            if (parts.length >= 2) {
                orderInstructions.add(parts[1]);
            }
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (int i = 0; i < orderInstructions.size() / 2; i++) {
            String propertyName = orderInstructions.get(2 * i);
            String directionsAsString = orderInstructions.get(2 * i + 1);
            Sort.Direction direction = (directionsAsString.equalsIgnoreCase("desc")) ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            orders.add(new Sort.Order(direction, propertyName));
        }
        return Sort.by(orders);
    }

    private Pagination(Page page, Params params, int maxPaginationLinks) {
        this.page = page;
        this.params = params;
        this.maxPaginationLinks = maxPaginationLinks;
        generateLinks();
    }

    private void generateLinks() {
        int thisPageIndex = page.getNumber();
        int maxPageIndex = page.getTotalPages() - 1;

        SortedSet<Integer> pagesToGenerateSet = new TreeSet<>();

        pagesToGenerateSet.add(0);
        pagesToGenerateSet.add(maxPageIndex);

        int offset = Math.max(maxPaginationLinks / 2, 1); // should be >=1 to create prev and next links
        for (int pageIndex = Math.max(thisPageIndex - offset, 1);
                pageIndex <= Math.min(thisPageIndex + offset, maxPageIndex);
                pageIndex++) {
            pagesToGenerateSet.add(pageIndex);
        }

        pageLinks = new ArrayList<>();
        List<Integer> pageIndices = new ArrayList<>(pagesToGenerateSet);
        for (int i = 0; i < pageIndices.size(); i++) {
            int pageIndex = pageIndices.get(i);
            String link = getParams().put("page", Integer.toString(pageIndex)).toString();
            pageLinks.add(new LinkInfo(
                            link,
                            Integer.toString(pageIndex + 1),
                            pageIndex == thisPageIndex));
            if (i + 1 < pageIndices.size() && pageIndices.get(i + 1) != pageIndex + 1) {
                pageLinks.add(new LinkInfo("#", "...", false, true));
            }
        }

    }

    public boolean hasSorting(String property, String direction) {
        String pattern = property + "," + (!direction.isBlank() ? direction : "");
        return Arrays.stream(params.sort()).anyMatch(v -> v.startsWith(pattern));
    }

    public boolean hasSorting(String property) {
        return hasSorting(property, "");
    }

    public String sortSwitchingLink(String fieldName) {
        String direction = (hasSorting(fieldName, "asc")) ? "desc" : "asc";
        return params.put("sort", fieldName + "," + direction).toString();
    }

    @Setter
    @Getter
    public static class LinkInfo {
        private String link;
        private String title;
        private boolean active;
        private boolean disabled;

        public LinkInfo(String link, String title) {
            this.link = link;
            this.title = title;
        }

        public LinkInfo(String link, String title, boolean active) {
            this.link = link;
            this.title = title;
            this.active = active;
        }

        public LinkInfo(String link, String title, boolean active, boolean disabled) {
            this.link = link;
            this.title = title;
            this.active = active;
            this.disabled = disabled;
        }
    }

    @Getter
    public static class Params {
        private final MultiValueMap<String, String> paramsMap;

        public Params(MultiValueMap<String, String> paramsAsMap) {
            this.paramsMap = new LinkedMultiValueMap<>(paramsAsMap);
        }

        public static Params of(MultiValueMap<String, String> paramsAsMap) {
            return new Params(paramsAsMap);
        }

        public Params put(String name, String value) {
            Params newInstance = new Params(this.getParamsMap());
            newInstance.getParamsMap().put(name, List.of(value));
            return newInstance;
        }

        public Params remove(String name) {
            Params newInstance = new Params(this.getParamsMap());
            newInstance.getParamsMap().remove(name);
            return newInstance;
        }

        public boolean contains(String name) {
            return getParamsMap().containsKey(name);
        }

        public String searchPattern() {
            String value = getParamsMap().getFirst("search");
            if (value == null) {
                value = "";
            }
            return value;
        }

        public int pageIndex() {
            String value = getParamsMap().getFirst("page");
            if (value == null) {
                value = "0";
            }
            return Integer.parseInt(value);
        }

        public String[] sort() {
            List<String> value = getParamsMap().get("sort");
            if (value != null) {
                return value.toArray(new String[0]);
            } else {
                return new String[0];
            }
        }

        public int pageSize() {
            String value = getParamsMap().getFirst("size");
            if (value == null) {
                value = "0";
            }
            return Integer.parseInt(value);
        }

        @Override
        public String toString() {
            List<String> pairs = new LinkedList<>();
            getParamsMap().forEach((k, listOfV) -> {
                for (String v : listOfV) {
                    pairs.add(k + "=" + v);
                }
            });
            return String.join("&", pairs);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Params params = (Params) o;

            return paramsMap.equals(params.paramsMap);
        }

        @Override
        public int hashCode() {
            return paramsMap.hashCode();
        }
    }

}
