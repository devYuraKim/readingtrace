import { apiClient } from '@/queries/axios';
import { useCustomShelves } from '@/queries/useCustomShelves';
import { useDefaultShelves } from '@/queries/useDefaultShelves';
import { useAuthStore, UserProfile } from '@/store/useAuthStore';
import { CustomShelf, DefaultShelf } from '@/types/shelf.types';
import { useQuery } from '@tanstack/react-query';
import { ChevronRight } from 'lucide-react';
import { useLocation } from 'react-router-dom';
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
import { NavUser } from './NavUser';

const data = {
  navMain: [
    {
      title: 'Book Search',
      items: [
        {
          title: 'Collection',
          url: '/users/userId/collection',
        },
        {
          title: 'Search',
          url: '/users/userId/search',
        },
      ],
    },
    { title: 'Default Bookshelf' },
    { title: 'Custom Bookshelf' },
    {
      title: 'Community',
      items: [
        { title: 'Chat', url: '/users/userId/community/chats' },
        { title: 'Meetup', url: '/users/userId/community/meetups' },
      ],
    },
  ],
};

type SidebarMenuItem = {
  title: string;
  url: string;
};

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  const location = useLocation();

  const userId = useAuthStore((state) => state.user?.userId);
  const { data: customShelves, isPending: isPendingCustom } =
    useCustomShelves(userId);
  const { data: defaultShelves, isPending: isPendingDefault } =
    useDefaultShelves(userId);

  const getCombinedNavData = (
    customShelves: CustomShelf[],
    defaultShelves: DefaultShelf[],
  ) => {
    // 1. Static items that will always be rendered
    const staticNavMain = [...data.navMain];

    // 2. Transform the dynamic shelf data if it exists
    let customShelvesItems: SidebarMenuItem[];
    let defaultShelvesItems: SidebarMenuItem[];

    if (customShelves && customShelves.length > 0) {
      customShelvesItems = [...customShelves]
        .sort((a, b) => a.orderIndex - b.orderIndex)
        .map((shelf) => ({
          title: `${shelf.name} (${shelf.bookCount})`,
          url: `/users/${shelf.userId}/books?shelfId=${shelf.shelfId}`,
        }));
    }

    if (defaultShelves && defaultShelves.length > 0) {
      defaultShelvesItems = [...defaultShelves]
        .sort((a, b) => a.orderIndex - b.orderIndex)
        .map((shelf) => ({
          title: `${shelf.name} (${shelf.bookCount})`,
          url: `/users/${shelf.userId}/books?shelfSlug=${shelf.slug}`,
        }));
    }

    // 3. Find the 'Custom Bookshelf' parent item in the static structure
    const customBookShelfParent = staticNavMain.find(
      (item) => item.title === 'Custom Bookshelf',
    );
    const defaultBookShelfParent = staticNavMain.find(
      (item) => item.title === 'Default Bookshelf',
    );

    // 4. Replace the hardcoded items array with the shelf items
    if (customBookShelfParent) {
      customBookShelfParent.items = customShelvesItems;
    }
    if (defaultBookShelfParent) {
      defaultBookShelfParent.items = defaultShelvesItems;
    }

    return staticNavMain;
  };
  const combinedNavData = getCombinedNavData(customShelves, defaultShelves);

  const { data: userProfile, isPending: isPendingUserProfile } =
    useQuery<UserProfile>({
      queryKey: ['userProfile', userId],
      queryFn: async () => {
        const res = await apiClient.get(`/users/${userId}/profile`);
        return res.data;
      },
    });

  return (
    <>
      <Sidebar {...props}>
        <SidebarHeader>
          <NavUser
            profileImageUrl={userProfile?.profileImageUrl}
            nickname={userProfile?.nickname}
          />
          <SearchForm />
        </SidebarHeader>
        {!isPendingCustom && !isPendingDefault && combinedNavData && (
          <SidebarContent className="gap-0">
            {/* We create a collapsible SidebarGroup for each parent. */}
            {combinedNavData.map((item) => (
              <Collapsible
                key={item.title}
                title={item.title}
                className="group/collapsible"
              >
                <SidebarGroup>
                  <SidebarGroupLabel
                    asChild
                    className="group/label text-sidebar-foreground hover:bg-sidebar-accent hover:text-sidebar-accent-foreground text-sm cursor-pointer"
                  >
                    <CollapsibleTrigger>
                      {item.title}{' '}
                      <ChevronRight className="ml-auto transition-transform group-data-[state=open]/collapsible:rotate-90" />
                    </CollapsibleTrigger>
                  </SidebarGroupLabel>
                  <CollapsibleContent>
                    <SidebarGroupContent>
                      <SidebarMenu>
                        {item.items?.map((subItem) => {
                          const url = new URL(
                            subItem.url,
                            window.location.origin,
                          );
                          const isActive =
                            location.pathname === url.pathname &&
                            location.search === url.search;
                          return (
                            <SidebarMenuItem key={subItem.title}>
                              <SidebarMenuButton
                                asChild
                                isActive={isActive}
                                className="pl-7"
                              >
                                <a href={subItem.url}>{subItem.title}</a>
                              </SidebarMenuButton>
                            </SidebarMenuItem>
                          );
                        })}
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
