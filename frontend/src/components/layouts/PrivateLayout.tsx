import { Fragment } from 'react/jsx-runtime';
import { useCustomShelves } from '@/queries/useCustomShelves';
import { useDefaultShelves } from '@/queries/useDefaultShelves';
import { useAuthStore } from '@/store/useAuthStore';
import { CustomShelf, DefaultShelf } from '@/types/shelf.types';
import { Outlet, useLocation } from 'react-router-dom';
import { AppSidebar } from '../Sidebar/AppSidebar';
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbSeparator,
} from '../ui/breadcrumb';
import { Separator } from '../ui/separator';
import { SidebarInset, SidebarProvider, SidebarTrigger } from '../ui/sidebar';

export const nav = [
  {
    parentTitle: 'Book Search',
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
  { parentTitle: 'Default Bookshelf' },
  { parentTitle: 'Custom Bookshelf' },
  {
    parentTitle: 'Community',
    items: [
      { title: 'Friend', url: '/users/userId/community/friends' },
      { title: 'Chat', url: '/users/userId/community/chats' },
      { title: 'Meetup', url: '/users/userId/community/meetups' },
    ],
  },
];

export default function PrivateLayout() {
  const userId = useAuthStore((state) => state.user?.userId);

  const location = useLocation();
  const currentPath = location.pathname + location.search;

  const { data: customShelves } = useCustomShelves(userId);
  const { data: defaultShelves } = useDefaultShelves(userId);

  const getCombinedNav = (
    customShelves: CustomShelf[],
    defaultShelves: DefaultShelf[],
  ) => {
    // 1. Base nav
    const baseNav = nav.map((section) => ({
      ...section,
      items: section.items?.map((item) => ({
        ...item,
        url: item.url.replace('userId', userId),
      })),
    }));

    // 2. Dynamic shelf data
    const customShelfItems = customShelves
      ? [...customShelves]
          .sort((a, b) => a.orderIndex - b.orderIndex)
          .map((shelf) => ({
            title: `${shelf.name} (${shelf.bookCount})`,
            url: `/users/${shelf.userId}/books?shelfId=${shelf.shelfId}`,
          }))
      : [];

    const defaultShelfItems = defaultShelves
      ? [...defaultShelves]
          .sort((a, b) => a.orderIndex - b.orderIndex)
          .map((shelf) => ({
            title: `${shelf.name} (${shelf.bookCount})`,
            url: `/users/${shelf.userId}/books?shelfSlug=${shelf.slug}`,
          }))
      : [];

    // 3. Find 'Custom Bookshelf' parent in the static structure
    const customBookShelfParent = baseNav.find(
      (parent) => parent.parentTitle === 'Custom Bookshelf',
    );
    const defaultBookShelfParent = baseNav.find(
      (parent) => parent.parentTitle === 'Default Bookshelf',
    );

    // 4. Replace the hardcoded items array with the shelf items
    if (customBookShelfParent) customBookShelfParent.items = customShelfItems;
    if (defaultBookShelfParent)
      defaultBookShelfParent.items = defaultShelfItems;

    return baseNav;
  };

  const completeNav = getCombinedNav(customShelves, defaultShelves);

  const findBreadcrumbs = (nav: typeof completeNav, path: string) => {
    for (const parent of nav) {
      if (!parent.items) continue;
      for (const item of parent.items) {
        if (item.url === path) {
          return [parent.parentTitle, item.title];
        }
      }
    }
    return [];
  };

  const breadcrumbs = findBreadcrumbs(completeNav, currentPath);

  return (
    <SidebarProvider>
      <AppSidebar completeNav={completeNav} />
      <SidebarInset>
        <header className="bg-background sticky top-0 flex h-16 shrink-0 items-center gap-2 border-b px-4 z-10">
          <SidebarTrigger className="-ml-1" />
          <Separator orientation="vertical" className="mr-2 h-4" />
          <Breadcrumb>
            <BreadcrumbList>
              {breadcrumbs.map((title, index) => (
                <Fragment key={index}>
                  <BreadcrumbItem
                    className={
                      index === breadcrumbs.length - 1 ? '' : 'hidden md:block'
                    }
                  >
                    <BreadcrumbLink href="#">{title}</BreadcrumbLink>
                  </BreadcrumbItem>
                  {index < breadcrumbs.length - 1 && (
                    <BreadcrumbSeparator className="hidden md:block" />
                  )}
                </Fragment>
              ))}
            </BreadcrumbList>
          </Breadcrumb>
        </header>
        <div className="flex flex-1 flex-col gap-4 p-4">
          <Outlet />
        </div>
      </SidebarInset>
    </SidebarProvider>
  );
}
