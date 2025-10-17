import { useUserShelves } from '@/queries/useUserShelves';
import { useAuthStore } from '@/store/useAuthStore';
import { ChevronRight } from 'lucide-react';
import { SearchForm } from '@/components/SearchForm';
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@/components/ui/collapsible';
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarRail,
} from '@/components/ui/sidebar';
import { VersionSwitcher } from '@/components/VersionSwitcher';
import { Shelf } from '@/lib/shelves';

// This is sample data.
const data = {
  versions: ['1.0.1', '1.1.0-alpha', '2.0.0-beta1'],
  navMain: [
    { title: 'Your Bookshelf' },
    {
      title: 'Static',
      url: '#',
      items: [
        {
          title: 'static sub',
          url: '#',
        },
      ],
    },
  ],
};

type SidebarMenuItem = {
  title: string;
  url: string;
};

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  const userId = useAuthStore((state) => state.user?.userId);
  const { data: shelves, isPending } = useUserShelves(userId);

  const getCombinedNavData = (shelves: Shelf[]) => {
    // 1. Static items that will always be rendered
    const staticNavMain = [...data.navMain];

    // 2. Transform the dynamic shelf data if it exists
    let shelvesItems: SidebarMenuItem[];
    if (shelves && shelves.length > 0) {
      shelvesItems = shelves.map((shelf) => ({
        title: `${shelf.name} (${shelf.bookCount})`,
        url: `/users/${shelf.userId}/books?shelfId=${shelf.shelfId}`,
      }));
    }

    // 3. Find the 'Your Bookshelf' parent item in the static structure
    const bookshelfParent = staticNavMain.find(
      (item) => item.title === 'Your Bookshelf',
    );

    // 4. Replace the hardcoded items array with the shelf items
    if (bookshelfParent) {
      bookshelfParent.items = shelvesItems;
    }

    return staticNavMain;
  };
  const combinedNavData = getCombinedNavData(shelves);
  console.log(combinedNavData);

  return (
    <>
      <Sidebar {...props}>
        <SidebarHeader>
          <VersionSwitcher
            versions={data.versions}
            defaultVersion={data.versions[0]}
          />
          <SearchForm />
        </SidebarHeader>
        {!isPending && combinedNavData && (
          <SidebarContent className="gap-0">
            {/* We create a collapsible SidebarGroup for each parent. */}
            {combinedNavData.map((item) => (
              <Collapsible
                key={item.title}
                title={item.title}
                defaultOpen
                className="group/collapsible"
              >
                <SidebarGroup>
                  <SidebarGroupLabel
                    asChild
                    className="group/label text-sidebar-foreground hover:bg-sidebar-accent hover:text-sidebar-accent-foreground text-sm"
                  >
                    <CollapsibleTrigger>
                      {item.title}{' '}
                      <ChevronRight className="ml-auto transition-transform group-data-[state=open]/collapsible:rotate-90" />
                    </CollapsibleTrigger>
                  </SidebarGroupLabel>
                  <CollapsibleContent>
                    <SidebarGroupContent>
                      <SidebarMenu>
                        {item.items.map((item) => (
                          <SidebarMenuItem key={item.title}>
                            <SidebarMenuButton
                              asChild
                              isActive={item.isActive}
                              className="ml-2"
                            >
                              <a href={item.url}>{item.title}</a>
                            </SidebarMenuButton>
                          </SidebarMenuItem>
                        ))}
                      </SidebarMenu>
                    </SidebarGroupContent>
                  </CollapsibleContent>
                </SidebarGroup>
              </Collapsible>
            ))}
          </SidebarContent>
        )}
        <SidebarRail />
      </Sidebar>
    </>
  );
}
