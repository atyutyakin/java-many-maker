package pro.alxerxc.menuMaker.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.*;

@Getter
public class Pagination {
    private static final int DEFAULT_MAX_PAGINATION_LINKS = 5;

    private Page page;
    private List<LinkInfo> links;
    private int maxPaginationLinks;

    private Pagination(Page page, int maxPaginationLinks) {
        this.page = page;
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

        links = new ArrayList<>();
        List<Integer> pageIndices = new ArrayList<>(pagesToGenerateSet);
        for (int i = 0; i < pageIndices.size(); i++) {
            int pageIndex = pageIndices.get(i);
            links.add(new LinkInfo(
                            getLink(pageIndex),
                            Integer.toString(pageIndex + 1),
                            pageIndex == thisPageIndex));
            if (i + 1 < pageIndices.size() && pageIndices.get(i + 1) != pageIndex + 1) {
                links.add(new LinkInfo("#", "...", false, true));
            }
        }

    }

    private String getLink(int pageIndex) {
        return "page=" + pageIndex + "&size=" + page.getSize();
    }

    public static Pagination of(Page page, int maxPaginationLinks) {
        return new Pagination(page, maxPaginationLinks);
    }
    public static Pagination of(Page page) {
        return new Pagination(page, DEFAULT_MAX_PAGINATION_LINKS);
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
