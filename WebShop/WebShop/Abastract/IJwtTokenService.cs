using WebShop.Data.Entitties.Identity;

namespace WebShop.Abastract
{
    public interface IJwtTokenService
    {
        Task<string> CreateToken(UserEntity user);
    }
}
