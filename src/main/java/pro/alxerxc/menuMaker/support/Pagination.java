package pro.alxerxc.menuMaker.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.*;

@Getter
public class Pagination {
    private static final int DEFAULT_MAX_PAGINATION_LINKS = 5;

    private Page page;
    private List<LinkInfo> pageLinks;
    private int maxPaginationLinks;
    private String sortAsString;
    private String searchPattern;

    public static Pagination of(Page page, String searchPattern, int maxPaginationLinks) {
        return new Pagination(page, searchPattern, maxPaginationLinks);
    }
    public static Pagination of(Page page, String searchPattern) {
        return of(page, searchPattern, DEFAULT_MAX_PAGINATION_LINKS);
    }

    /** Creates Sort by given string array of property names and directions.
     *
     * @param sortClauses can be: {"name,asc"} or {"id,desc","name,asc"} or {"name", "asc"} (by pairs)
     * @return tuned Sort object
     */
    public static Sort sort(String[] sortClauses) {
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

    private Pagination(Page page, String searchPattern, int maxPaginationLinks) {
        this.page = page;
        this.maxPaginationLinks = maxPaginationLinks;
        this.sortAsString = sortParams();
        this.searchPattern = searchPattern;
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
            pageLinks.add(new LinkInfo(
                            getLink(pageIndex),
                            Integer.toString(pageIndex + 1),
                            pageIndex == thisPageIndex));
            if (i + 1 < pageIndices.size() && pageIndices.get(i + 1) != pageIndex + 1) {
                pageLinks.add(new LinkInfo("#", "...", false, true));
            }
        }

    }

    private String getLink(int pageIndex) {
        return "page=" + pageIndex
                + "&size=" + page.getSize()
                + sortParams()
                + (!searchPattern.isBlank() ? "&search=" + searchPattern : "");
    }

    private String sortParams() {
        StringBuilder sb = new StringBuilder();
        for (Sort.Order order: page.getSort()) {
            sb.append("&sort=");
            sb.append(order.getProperty());
            sb.append(",");
            sb.append(order.getDirection().toString().toLowerCase());
        }
        return sb.toString();
    }

    public boolean hasSorting(String property, String direction) {
        String pattern = "&sort=" + property.toLowerCase()
                + (!direction.isBlank() ? "," + direction.toLowerCase() : "");
        return sortAsString.toLowerCase().contains(pattern);
    }
    public boolean hasSorting(String property) {
        return hasSorting(property, "");
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
        private final Map<String, String> paramsMap;

        public Params(Map<String, String> paramsAsMap) {
            this.paramsMap = new HashMap(paramsAsMap);
        }

        public static Params of(Map<String, String> paramsAsMap) {
            return new Params(paramsAsMap);
        }

        public Params put(String name, String value) {
            Params newInstance = new Params(this.getParamsMap());
            newInstance.getParamsMap().put(name, value);
            return newInstance;
        }

        public Params remove(String name) {
            Params newInstance = new Params(this.getParamsMap());
            newInstance.getParamsMap().remove(name);
            return newInstance;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            getParamsMap().forEach((k, v) -> {
                if (sb.length() != 0) {
                    sb.append("&");
                }
                sb.append(k + "=" + v);
            });
            return sb.toString();
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
