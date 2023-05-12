using Microsoft.AspNetCore.Identity;

namespace WebShop.Data.Entitties.Identity
{
    public class RoleEntity : IdentityRole<int>
    {
        public virtual ICollection<UserRoleEntity> UserRoles { get; set; }
    }
}
