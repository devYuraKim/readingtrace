import { useAuthStore } from '@/store/useAuthStore';
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
import { nav } from '../layouts/PrivateLayout';
import { NavUser } from './NavUser';

type AppSidebarProps = React.ComponentProps<typeof Sidebar> & {
  completeNav: typeof nav;
};

export function AppSidebar({ completeNav, ...props }: AppSidebarProps) {
  const location = useLocation();
  const userProfile = useAuthStore((state) => state.userProfile);

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
        {completeNav && (
          <SidebarContent className="gap-0">
            {/* We create a collapsible SidebarGroup for each parent. */}
            {completeNav.map((parent) => (
              <Collapsible
                key={parent.parentTitle}
                title={parent.parentTitle}
                className="group/collapsible"
              >
                <SidebarGroup>
                  <SidebarGroupLabel
                    asChild
                    className="group/label text-sidebar-foreground hover:bg-sidebar-accent hover:text-sidebar-accent-foreground text-sm cursor-pointer"
                  >
                    <CollapsibleTrigger>
                      {parent.parentTitle}{' '}
                      <ChevronRight className="ml-auto transition-transform group-data-[state=open]/collapsible:rotate-90" />
                    </CollapsibleTrigger>
                  </SidebarGroupLabel>
                  <CollapsibleContent>
                    <SidebarGroupContent>
                      <SidebarMenu>
                        {parent.items?.map((children) => {
                          const url = new URL(
                            children.url,
                            window.location.origin,
                          );
                          const isActive =
                            location.pathname === url.pathname &&
                            location.search === url.search;
                          return (
                            <SidebarMenuItem key={children.title}>
                              <SidebarMenuButton
                                asChild
                                isActive={isActive}
                                className="pl-7"
                              >
                                <a href={children.url}>{children.title}</a>
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
