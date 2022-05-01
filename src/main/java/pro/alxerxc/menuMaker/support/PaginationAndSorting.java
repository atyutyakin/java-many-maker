package pro.alxerxc.menuMaker.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Getter
public class PaginationAndSorting {
    private static final int DEFAULT_MAX_PAGINATION_LINKS = 5;

    private Page page;
    private List<LinkInfo> pageLinks;
    private int maxPaginationLinks;
    private String sortAsString;

    public static PaginationAndSorting of(Page page, int maxPaginationLinks) {
        return new PaginationAndSorting(page, maxPaginationLinks);
    }
    public static PaginationAndSorting of(Page page) {
        return of(page, DEFAULT_MAX_PAGINATION_LINKS);
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

    private PaginationAndSorting(Page page, int maxPaginationLinks) {
        this.page = page;
        this.maxPaginationLinks = maxPaginationLinks;
        this.sortAsString = sortParams();
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
        return "page=" + pageIndex + "&size=" + page.getSize() + sortParams();
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

    public boolean hasAscSorting(String property) {
        return sortAsString.toLowerCase().contains("&sort=" + property + ",asc");
    }

    @Setter
    @Getter
    public class LinkInfo {
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

}
